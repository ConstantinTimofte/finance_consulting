package com.investing.tracker.controller;

import com.investing.tracker.model.dto.InvestingTrkDto;
import com.investing.tracker.service.TRackerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/track")
@AllArgsConstructor
public class TrackingController {

    private final TRackerService tRackerService;

    @RequestMapping(value = "/{firstname}/{lastname}/{investingname}", method = RequestMethod.GET)
    public List<InvestingTrkDto> search(@PathVariable("firstname") String firstName, @PathVariable("lastname") String lastName
            , @PathVariable("investingname") String investingName) {

        return tRackerService.search(firstName, lastName, investingName);
    }
}
