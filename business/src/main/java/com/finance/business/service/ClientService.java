package com.finance.business.service;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.data.repository.InvestmentRepository;
import com.model.client.ClientDto;
import com.model.clientinvest.ClientInvestmentDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ClientService {


    private final ClientRepository clientRepository;
    private final InvestmentRepository investmentRepository;
    private final ClientInvestmentRepository clientInvestmentRepository;
    private final ModelMapper modelMapper;
    private final InvestmentService investmentService;


    @Transactional
    public void savingInvestment(ClientInvestmentDto clientInvestmentDto) {

        Client client = clientRepository.findClientByFirstNameAndLastName(clientInvestmentDto.getFirstName(), clientInvestmentDto.getSecondName());
        if (!clientInvestmentDto.getClient()) {
            /** The contact become client */
            client.setClient(true);
            client.setPayment(true);
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
                .activationInvestment(LocalDate.now())
                .statusOfPayment(true)/** paid investment */
                .build();

        clientInvestmentRepository.save(clientInvestment);
    }


    public List<String> findClientInvestments(String firstName, String secondName) {
        List<String> investmentsThatClientCanInvest;
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

    public List<ClientDto> search(String searchTerm , String contactType, String payment){
        Boolean isClient = isClient(contactType);
        Boolean isPay = payment(payment);
        searchTerm = searchTerm.trim().isEmpty() ? null : searchTerm;

        List<Client> clientEntityList = clientRepository.search(searchTerm);

            return  clientEntityList
                .stream()
                .filter(client -> isClient == null || client.getClient() == isClient)
                .filter(client -> isPay == null || client.getPayment() == isPay)
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());

    }

        // wirte me a fuction that takes List<Client> clientEntityList input ,
        // and return List<ClientDto> clientEntityList output by adding filter on isClient and isPay

    public void findAllClients(Client client, List<ClientDto> clientEntityList) {
        if (client.getPayment() != null) {/*SE CLIENTE*/
            List<ClientInvestment> getClientInvestments = clientInvestmentRepository.getClientInvestments(client.getId());

            for (ClientInvestment clientInvestment : getClientInvestments) {
                if (clientInvestment.getStatusOfPayment()) {
                    LocalDate expirationDate = investmentService.expirationDate(clientInvestment.getActivationInvestment(), clientInvestment.getMounth());
                    setStatusOfPayment(investmentService.remainingDays(expirationDate, LocalDate.now()), clientInvestment, client);
                }
            }

        }
        clientEntityList.add(modelMapper.map(client, ClientDto.class));
    }

    @Transactional
    Boolean setStatusOfPayment(Integer remainingDays, ClientInvestment clientInvestment, Client client) {
        if (remainingDays == null) {
            return true;
        } else if (remainingDays == 0) {
            /** UPDATE STATUS */
            clientInvestment.setStatusOfPayment(false);
            clientInvestmentRepository.save(clientInvestment);

            client.setPayment(false);
            clientRepository.save(client);
            return false;
        }
        /*IL DEFAULT E SEMPRE TRUE*/
        return true;
    }


    public Boolean isClient(String contacType) {
        if (contacType != null) {
            return contacType.contains("CLIENT");
        }
        return null;
    }

    public Boolean payment(String payment) {
        if (payment != null) {
            return payment.contains("Y");
        }
        return null;
    }

}
