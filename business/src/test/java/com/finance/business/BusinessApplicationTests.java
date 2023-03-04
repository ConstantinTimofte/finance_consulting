package com.finance.business;

import com.finance.business.data.entity.Client;
import com.finance.business.data.entity.ClientInvestment;
import com.finance.business.data.entity.Investment;
import com.finance.business.data.repository.ClientInvestmentRepository;
import com.finance.business.data.repository.InvestmentRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BusinessApplicationTests {

    @Autowired
    ClientInvestmentRepository clientInvestmentRepository;

    @Autowired
    InvestmentRepository investmentRepository;

    @Test
    void contextLoads() {
    }


    @Transactional
    public void testSelect() {
        List<ClientInvestment> clientInvestmentList = clientInvestmentRepository.findClientInvestmentsList(11); // FETCH join ACCESS
        System.out.println(clientInvestmentList.get(0).getIdClient());

        ClientInvestment clientInvestment = clientInvestmentRepository.findById(137).get();/*LAZY ACCESS */
        Client client  = clientInvestment.getIdClient();

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

/*      List<ClientInvestment> clientInvestmentList = new ArrayList<>();
        clientInvestmentList.addAll(investmentB.getClientInvestments());
        clientInvestmentList.addAll(investmentC.getClientInvestments());
        clientInvestmentRepository.deleteAllInBatch(clientInvestmentList);*/

       // investmentRepository.deleteAllInBatch(cc);

        System.out.println("finish");
    }
}
