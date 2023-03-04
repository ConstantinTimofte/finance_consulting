package com.investing.tracker.service;

import com.investing.tracker.model.dto.InvestingTrkDto;
import com.investing.tracker.model.entity.InvestingTrk;
import com.investing.tracker.repo.TrackerRepository;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class TRackerService {

    private final TrackerRepository trackerRepository;


    public List<InvestingTrkDto> search(String firstName, String latName, String investingName) {
              trackerRepository.search(firstName, latName, investingName);
              return null;
    }
}
