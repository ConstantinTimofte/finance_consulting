package com.clients.service;


import com.model.clientinvest.ClientInvestmentsFeign;
import com.model.clientinvest.SearchInvestmentDto;
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

    public void saveChangedInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        clientInvestmentsFeign.modifySavedInvestment(investmentsOfClientsDto);
    }

    public void deleteInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        clientInvestmentsFeign.deletetSavedInvestment(investmentsOfClientsDto);
    }

    public void activateExpiredInvestment(InvestmentsOfClientsDto investmentsOfClientsDto) {
        clientInvestmentsFeign.activateexpiredinvestment(investmentsOfClientsDto);
    }

    public List<InvestmentsOfClientsDto> searchInvestments(SearchInvestmentDto searchInvestmentDto) {
        return clientInvestmentsFeign.searchInvestment(searchInvestmentDto);
    }
}
