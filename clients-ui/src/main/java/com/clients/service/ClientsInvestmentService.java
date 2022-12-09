package com.clients.service;


import com.model.clientinvest.ClientInvestmentsFeign;
import com.model.investment.InvestmentsOfClientsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientsInvestmentService {

    private final ClientInvestmentsFeign clientInvestmentsFeign;

    public List<InvestmentsOfClientsDto> getAll() {
        return clientInvestmentsFeign.getAll();
    }
}
