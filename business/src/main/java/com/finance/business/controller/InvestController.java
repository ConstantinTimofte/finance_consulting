package com.finance.business.controller;


import com.finance.business.service.InvestmentService;
import com.model.investment.InvestmentsOfClientsDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/activateexpiredinvestment",method = RequestMethod.POST)
    public void activateexpiredinvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto){
        investmentService.activateExpiredInvestment(investmentsOfClientsDto);
    }
}
