package com.finance.business.service;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.data.repository.InvestmentRepository;
import com.model.investment.InvestmentsOfClientsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.findAll();

        for (ClientInvestment clientInvestment : clientInvestmentList) {
            Optional<Client> client = clientRepository.findById(clientInvestment.getIdClient().getId());
            Investment investment = investmentRepository.getById(clientInvestment.getIdInvestment().getId());

            LocalDate expirationDate = expirationDate(clientInvestment.getActivationInvestment(), clientInvestment.getMounth());

            InvestmentsOfClientsDto investmentsOfClientsDto = InvestmentsOfClientsDto.builder()
                    .firstName(client.get().getFirstName())
                    .lastName(client.get().getLastName())
                    .investmentName(investment.getName())
                    .statusOfPayment(clientInvestment.getStatusOfPayment())
                    .expiringDate(formatLocalDate("dd-MM-YYYY",expirationDate))
                    .remainingDays(remainingDays(expirationDate, LocalDate.now()))
                    .mounth(clientInvestment.getMounth())
                    .sum(clientInvestment.getInvestment())
                    .build();


            allInvestmentsOfClients.add(investmentsOfClientsDto);
        }
        return allInvestmentsOfClients;
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


}


