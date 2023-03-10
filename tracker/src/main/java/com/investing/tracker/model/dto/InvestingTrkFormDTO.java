package com.investing.tracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvestingTrkFormDTO {

    private LocalDate initialDate;
    private List<InvestingTrkDto> investingTrkDtoList;
}
