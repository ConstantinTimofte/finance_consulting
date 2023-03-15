package com.finance.business;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.data.repository.InvestmentRepository;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BusinessApplicationTests {

    @Autowired
    ClientInvestmentRepository clientInvestmentRepository;

    @Autowired
    InvestmentRepository investmentRepository;

    @Autowired
    ClientRepository clientRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void contextLoads() {
    }


    @Transactional
    @Disabled
    public void testSelect() {
        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.findClientInvestmentsList(11); // FETCH join ACCESS
        System.out.println(clientInvestmentList.get(0).getIdClient());

        ClientInvestment clientInvestment = clientInvestmentRepository.findById(137).get();/*LAZY ACCESS */
        Client client = clientInvestment.getIdClient();

    }

    @Test
    @Transactional
    public void testDelete() {
        //TODO CREA UN INVESTIMNENTO CHE NON HA UNA RELAZIONE CON  clientInvestments e vedi il risultato
        Investment investmentB = investmentRepository.findInvestmentByNameJOINTEST("CANCEL-1");//FETCH JOIN

        Investment investmentC = investmentRepository.findInvestmentByName("CANCEL-44");//GRAPH JOIN
        List<Investment> cc = new ArrayList<>();
        cc.add(investmentB);
        cc.add(investmentC);
        investmentRepository.deleteById(57);
/*      List<ClientInvestment> clientInvestmentList = new ArrayList<>();
        clientInvestmentList.addAll(investmentB.getClientInvestments());
        clientInvestmentList.addAll(investmentC.getClientInvestments());
        clientInvestmentRepository.deleteAllInBatch(clientInvestmentList);*/

        // investmentRepository.deleteAllInBatch(cc);

        System.out.println("finish");
    }


    @Test
    @Disabled
    @Transactional
    public void saveTest() {
        Client client = new Client();
        client.setClient(false);
        client.setPayment(true);
        client.setFirstName("PROVA");
        client.setLastName("priova");
        entityManager.merge(client);
        entityManager.flush();
        List<ClientInvestment> arr = new ArrayList<>();
        ClientInvestment clientInvestment = new ClientInvestment();
        clientInvestment.setIdClient(client);
        clientInvestment.setIdInvestment(entityManager.getReference(Investment.class, 56));
        clientInvestment.setInvestment(212121);
        clientInvestment.setStatusOfPayment(false);

        arr.add(clientInvestment);

       // clientRepository.save(client);
        // clientRepository.delete(client);


        System.out.println("finish");
    }

/*    @Test
    public void saveTestEntityManager() {
        Client client = new Client();
        client.setClient(false);
        client.setPayment(false);
        client.setFirstName("PROVA");
        client.setLastName("priova");

        entityManager.persist(client);
    }*/
}

