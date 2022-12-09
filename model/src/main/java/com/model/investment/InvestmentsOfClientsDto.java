package com.model.investment;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvestmentsOfClientsDto {

    private String firstName;
    private String lastName;
    private String investmentName;
    private Boolean statusOfPayment;/** Single investment*/
    private LocalDate expiringDate;
    private Integer remainingDays;
    private Integer mounth;/*Ogni quanto deve investire*/
    private Integer sum;
}
