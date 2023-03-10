package com.investing.tracker.controller;

import com.investing.tracker.model.dto.InvestingTrkFormDTO;
import com.investing.tracker.service.TRackerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/track")
@AllArgsConstructor
public class TrackingController {

    private final TRackerService tRackerService;

/*    @RequestMapping(value = {
            "/{firstname}/{lastname}/{investingname}",
            "/{firstname}//{investingname}",
            "/{firstname}/{lastname}",
            "/{firstname}",
            "//{lastname}"
    }, method = RequestMethod.GET)
    public InvestingTrkFormDTO search(@PathVariable(value = "firstname", required = false) String firstName,
                                      @PathVariable(value = "lastname", required = false) String lastName,
                                      @PathVariable(value = "investingname", required = false) String investingName) {

        return tRackerService.search(firstName, lastName, investingName);
    }*/


    @RequestMapping(value = "", method = RequestMethod.GET)
    public InvestingTrkFormDTO search(@RequestParam(value = "firstname", required = false) String firstName,
                                      @RequestParam(value = "lastname", required = false) String lastName,
                                      @RequestParam(value = "investingname", required = false) String investingName) {

        return tRackerService.search(firstName, lastName, investingName);
    }
}
