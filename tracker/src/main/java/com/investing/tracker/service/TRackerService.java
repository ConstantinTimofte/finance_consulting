package com.investing.tracker.service;

import com.investing.tracker.model.dto.InvestingTrkDto;
import com.investing.tracker.model.dto.InvestingTrkFormDTO;
import com.investing.tracker.model.entity.InvestingTrk;
import com.investing.tracker.repo.TrackerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TRackerService {

    private final TrackerRepository trackerRepository;
    private final ModelMapper modelMapper;

    public InvestingTrkFormDTO search(String firstName, String latName, String investingName) {
        InvestingTrkFormDTO InvestingTrkFormDTO = null;
        List<InvestingTrk> investingTrkList = trackerRepository.search(firstName, latName, investingName);


        List<InvestingTrkDto> InvestingTrkDtoLisT = investingTrkList.stream()
                .map(investingTrk -> modelMapper.map(investingTrk, InvestingTrkDto.class))
                .collect(Collectors.toList());


        if (InvestingTrkDtoLisT.size() > 0) {
            InvestingTrkFormDTO = new InvestingTrkFormDTO(InvestingTrkDtoLisT.get(0).getDayPay(), InvestingTrkDtoLisT);
        }
        return InvestingTrkFormDTO;
    }
}
