package com.model.clientinvest;


import com.model.investment.InvestmentsOfClientsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "investContextId",value = "business", path = "/api/invest")
public interface ClientInvestmentsFeign {

    @GetMapping("/all")
    List<InvestmentsOfClientsDto> getAll();


}
