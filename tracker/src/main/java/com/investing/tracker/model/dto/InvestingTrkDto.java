package com.investing.tracker.model.dto;

import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class InvestingTrkDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String investingName;
    private LocalDate dayPay;
    private Integer sum;
}
