package com.finance.business.service;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.data.repository.InvestmentRepository;
import com.model.clientinvest.ClientInvestmentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ClientService {


    private final ClientRepository clientRepository;
    private final InvestmentRepository investmentRepository;
    private final ClientInvestmentRepository clientInvestmentRepository;


    @Transactional
    public void savingInvestment(ClientInvestmentDto clientInvestmentDto) {

        Client client = clientRepository.findClientByFirstNameAndLastName(clientInvestmentDto.getFirstName(), clientInvestmentDto.getSecondName());
        if (!clientInvestmentDto.getClient()) {
            client.setClient(true);
        }


        Investment investment = investmentRepository.findInvestmentByName(clientInvestmentDto.getInvestmentName().toUpperCase());
        if (investment == null) {
            investment = new Investment(clientInvestmentDto.getInvestmentName().toUpperCase(), clientInvestmentDto.getInvestmentName().toUpperCase());
            investmentRepository.save(investment);
        }

        ClientInvestment clientInvestment = ClientInvestment.builder()
                .idClient(client)
                .idInvestment(investment)
                .investment(clientInvestmentDto.getSumToInvest())
                .mounth(clientInvestmentDto.getMounth())
                .activationInvestment(new Date())
                .build();

        clientInvestmentRepository.save(clientInvestment);
    }


    public List<String> findClientInvestments(String firstName, String secondName) {
        List<String> investmentsThatClientCanInvest = new ArrayList<>();
        Client client = clientRepository.findClientByFirstNameAndLastName(firstName, secondName);
        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.findClientInvestmentsList(client.getId());


        if (clientInvestmentList.isEmpty()) {/*Restituisco tutti gli investimenti */
            investmentsThatClientCanInvest = investmentRepository.findAll()
                    .stream()
                    .map(investment -> investment.getName())
                    .collect(Collectors.toList());

        } else {/*Il cliente ha gia fatto dei investimenti*/
            List<Integer> nameInvestment = clientInvestmentList
                    .stream()
                    .map(investment -> investment.getIdInvestment().getId())
                    .collect(Collectors.toList());

            investmentsThatClientCanInvest = investmentRepository.all(nameInvestment)
                    .stream()
                    .map(investment -> investment.getName())
                    .collect(Collectors.toList());
        }

        return investmentsThatClientCanInvest;
    }

}

