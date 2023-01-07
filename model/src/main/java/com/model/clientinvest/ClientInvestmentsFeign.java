package com.model.clientinvest;


import com.model.investment.InvestmentsOfClientsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
