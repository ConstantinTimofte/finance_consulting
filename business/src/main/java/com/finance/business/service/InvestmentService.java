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
                    .statusOfPayment(clientInvestment.getStatusOfPayment()) //* Status of a singel investment*//*
                    .expiringDate(expirationDate)
                    .remainingDays(remainingDays(expirationDate))
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
    private LocalDate expirationDate(LocalDate startInvestment, Integer mounthOfPay) {
        LocalDate date = startInvestment.plusMonths(mounthOfPay);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date.format(dateTimeFormatter);
        return date;
    }


    /**
     * Testing -  date_activation =  current date + 2 / current mount - 1 / year
     */
    private static Integer remainingDays(LocalDate expiringDate) {
        int expiringDay = expiringDate.getDayOfMonth();
        int dayNow = LocalDate.now().getDayOfMonth();
        if (LocalDate.now().getMonthValue() == expiringDate.getMonthValue()) { /*stesso mese */

            if ((expiringDay > dayNow) && (expiringDay - dayNow <= 7)) {/*Ultimi 7 giorni di appaiono*/
                return expiringDay - dayNow;
            } else if (expiringDay < dayNow) {
                return 0;
            } else {
                return null;
            }
        }
        return null;
    }
}


