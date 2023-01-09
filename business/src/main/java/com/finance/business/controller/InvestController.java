package com.finance.business.controller;


import com.finance.business.service.InvestmentService;
import com.model.clientinvest.SearchInvestmentDto;
import com.model.investment.InvestmentsOfClientsDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/invest")
public class InvestController {

    private final InvestmentService investmentService;

    /**
     * Get all investments of clients
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<InvestmentsOfClientsDto> getAll() {
        return investmentService.getAllInvestmentsOfClients();
    }

    @RequestMapping(value = "/modifysavedinvestment", method = RequestMethod.POST)
    public void modifySavedInvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto) {
        investmentService.modifySavedInvestment(investmentsOfClientsDto);
    }

    @RequestMapping(value = "/deletetsavedinvestment", method = RequestMethod.DELETE)
    public void deletetSavedInvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto) {
        investmentService.deletetSavedInvestment(investmentsOfClientsDto);
    }

    @RequestMapping(value = "/activateexpiredinvestment", method = RequestMethod.POST)
    public void activateexpiredinvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto) {
        investmentService.activateExpiredInvestment(investmentsOfClientsDto);
    }

    @RequestMapping(value = "/searchinvestment", method = RequestMethod.POST)
    public List<InvestmentsOfClientsDto> searchInvestment(@RequestBody SearchInvestmentDto searchInvestmentDto) throws Exception {
        return investmentService.searchInvestment(searchInvestmentDto);
    }
}
