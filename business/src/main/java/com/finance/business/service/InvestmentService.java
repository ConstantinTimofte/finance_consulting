package com.finance.business.service;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.data.repository.InvestmentRepository;
import com.model.clientinvest.SearchInvestmentDto;
import com.model.investment.InvestmentsOfClientsDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InvestmentService {

    private final ClientInvestmentRepository clientInvestmentRepository;
    private final InvestmentRepository investmentRepository;
    private final ClientRepository clientRepository;

    public List<InvestmentsOfClientsDto> getAllInvestmentsOfClients() {
        List<InvestmentsOfClientsDto> allInvestmentsOfClients = new ArrayList<>();
        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        createInvestmentsOfClientsDto(allInvestmentsOfClients, clientInvestmentList);
        return allInvestmentsOfClients;
    }

    public void modifySavedInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        Optional<ClientInvestment> clientInvestment = clientInvestmentRepository.findById(investmentsOfClientsDto.getId());
        clientInvestment.get().setInvestment(investmentsOfClientsDto.getSum());

        /** Se cambia il mese */
        if (investmentsOfClientsDto.getMounth() != clientInvestment.get().getMounth()) {
            clientInvestment.get().setMounth(investmentsOfClientsDto.getMounth());
        }
        clientInvestmentRepository.save(clientInvestment.get());
    }

    @Transactional
    public void deletetSavedInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        Client client = clientRepository.findClientByFirstNameAndLastName(investmentsOfClientsDto.getFirstName(), investmentsOfClientsDto.getLastName());

        ClientInvestment currentClientInvestment = clientInvestmentRepository.findById(investmentsOfClientsDto.getId()).get();
        clientInvestmentRepository.delete(currentClientInvestment);

        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.getClientInvestments(client.getId());
        if (clientInvestmentList.isEmpty()) {
            client.setClient(false);
            client.setPayment(null);
        } else {
            long countNonPayd = clientInvestmentRepository.findClientInvestmentsListWithPaymentStatus(client.getId(), false).size();
            client.setPayment(countNonPayd > 0 ? false : true);
        }
        clientRepository.save(client);
    }

    @Transactional
    public void activateExpiredInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        ClientInvestment currentClientInvestment = clientInvestmentRepository.findById(investmentsOfClientsDto.getId()).get();
        currentClientInvestment.setStatusOfPayment(true);
        currentClientInvestment.setActivationInvestment(LocalDate.now());
        clientInvestmentRepository.save(currentClientInvestment);

        // AGGIORNO STATO INVESTIMENTI CLIENTE
        Client client = clientRepository.findClientByFirstNameAndLastName(investmentsOfClientsDto.getFirstName(), investmentsOfClientsDto.getLastName());
        long countNonPayd = clientInvestmentRepository.findClientInvestmentsListWithPaymentStatus(client.getId(), false).size();
        client.setPayment(countNonPayd > 0 ? false : true);

        clientRepository.save(client);
    }

    public List<InvestmentsOfClientsDto> searchInvestment(SearchInvestmentDto searchInvestmentDto) throws Exception {
        List<InvestmentsOfClientsDto> investmentsOfClientsDtoList = new ArrayList<>();

        if (areAllParamsNull(searchInvestmentDto)) {
            investmentsOfClientsDtoList = getAllInvestmentsOfClients();
        } else {
            Boolean statusPayment = null;
            if ("Payd".equals(searchInvestmentDto.getStatus())) {
                statusPayment = true;
            } else if ("Expired".equals(searchInvestmentDto.getStatus())) {
                statusPayment = false;
            }

            List<ClientInvestment> investmentsOfClientsList = clientInvestmentRepository.findClientInvestmentByIdClientAndIdInvestmentAndStatusOfPayment(searchInvestmentDto.getFirstName(),
                    searchInvestmentDto.getLastName(), searchInvestmentDto.getInvestmentName(), statusPayment);

            createInvestmentsOfClientsDto(investmentsOfClientsDtoList, investmentsOfClientsList);
        }

        return investmentsOfClientsDtoList;
    }

    private void createInvestmentsOfClientsDto(List<InvestmentsOfClientsDto> investmentsOfClientsDtoList, List<ClientInvestment> investmentsOfClientsList) {
        for (ClientInvestment clientInvestment : investmentsOfClientsList) {
            Optional<Client> client = clientRepository.findById(clientInvestment.getIdClient().getId());
            Investment investment = investmentRepository.getById(clientInvestment.getIdInvestment().getId());
            LocalDate expirationDate = expirationDate(clientInvestment.getActivationInvestment(), clientInvestment.getMounth());

            InvestmentsOfClientsDto investmentsOfClientsDto = InvestmentsOfClientsDto.builder()
                    .id(clientInvestment.getId())
                    .firstName(client.get().getFirstName())
                    .lastName(client.get().getLastName())
                    .investmentName(investment.getName())
                    .statusOfPayment(clientInvestment.getStatusOfPayment())
                    .expiringDate(formatLocalDate("dd-MM-YYYY", expirationDate))
                    .remainingDays(remainingDays(expirationDate, LocalDate.now()))
                    .mounth(clientInvestment.getMounth())
                    .sum(clientInvestment.getInvestment())
                    .build();


            investmentsOfClientsDtoList.add(investmentsOfClientsDto);
        }
    }


    /**
     * Verifica se tutti i parametri dell'oggetto SearchInvestmentDto sono null.
     *
     * @param object L'oggetto SearchInvestmentDto da controllare.
     * @return true se tutti i parametri dell'oggetto sono null, false altrimenti.
     */
    private boolean areAllParamsNull(SearchInvestmentDto object) throws Exception {
        // Ottieni tutti i campi dell'oggetto
        Field[] fields = object.getClass().getDeclaredFields();
        // Scorri i campi uno per uno
        for (Field field : fields) {
            field.setAccessible(true);// Il campo e dichiarato private, cosi lo rendi accessibile
            try {
                // Controlla se il valore del campo è null
                if (field.get(object) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new Exception();
            }
        }
        return true;
    }

    /**
     * Remaining days when the status of payment change
     */
    public LocalDate expirationDate(LocalDate startInvestment, Integer mounthOfPay) {
        LocalDate date = startInvestment.plusMonths(mounthOfPay);
        return date;
    }

    private String formatLocalDate(String pattern, LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        String localDateString = date.format(dateTimeFormatter);
        return localDateString;
    }

    /**
     * Testing -  date_activation =  current date + 2 / current mount - 1 / year
     */

    public static Integer remainingDays(LocalDate expiringDate, LocalDate current) {
        if (expiringDate.getYear() > current.getYear()) {
            /*Ancora non scaduto*/
            System.out.println("Ancora non scaduto");
            return null;/*Non scaduto , e giorni non calcolabili*/
        } else if (expiringDate.getYear() < current.getYear()) {
            /*scaduto*/
            System.out.println("scaduto");
            return 0;

        } else if (expiringDate.getYear() == current.getYear()) {

            if (expiringDate.getMonthValue() > current.getMonthValue()) {
                /*Ancora non scaduto*/
                return null;/*Non scaduto , e giorni non calcolabili*/

            } else if (current.getMonthValue() > expiringDate.getMonthValue()) {
                /*scaduto*/
                System.out.println("scaduto");
                return 0;

            } else if (current.getMonthValue() == expiringDate.getMonthValue()) {
                if (expiringDate.getDayOfMonth() > current.getDayOfMonth()) {
                    /*Ancora non scaduto - il tempo rimanente viene calcolato solo se e nello stesso mese*/
                    return expiringDate.getDayOfMonth() - current.getDayOfMonth();
                }
                System.out.println("scaduto");
                return 0;
            }
        }
        return null;/*Non scaduto , e giorni non calcolabili*/
    }

    public Integer remainingDays(LocalDate expiringDate, LocalDate current, int CASO2) {
        Integer result;

        if (expiringDate.getYear() > current.getYear()) {
            result = null;
        } else if (expiringDate.getYear() < current.getYear() ||
                (expiringDate.getYear() == current.getYear() && expiringDate.getMonthValue() < current.getMonthValue()) ||
                (expiringDate.getYear() == current.getYear() && expiringDate.getMonthValue() == current.getMonthValue() && expiringDate.getDayOfMonth() < current.getDayOfMonth())) {
            result = 0;
        } else {
            result = expiringDate.getDayOfMonth() - current.getDayOfMonth();
        }

        return result;
    }

}


