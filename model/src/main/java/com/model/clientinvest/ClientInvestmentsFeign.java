package com.model.clientinvest;

import com.model.investment.InvestmentsOfClientsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "investContextId", value = "business", path = "/api/invest")
public interface ClientInvestmentsFeign {

    @GetMapping("/all")
    List<InvestmentsOfClientsDto> getAll();

    @PostMapping("/modifysavedinvestment")
    void modifySavedInvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto);

    @DeleteMapping("/deletetsavedinvestment")
    void deletetSavedInvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto);

    @PostMapping("/activateexpiredinvestment")
    void activateexpiredinvestment(@RequestBody InvestmentsOfClientsDto investmentsOfClientsDto);

    @PostMapping(value = "/searchinvestment")
    List<InvestmentsOfClientsDto> searchInvestment(@RequestBody SearchInvestmentDto searchInvestmentDto);
}