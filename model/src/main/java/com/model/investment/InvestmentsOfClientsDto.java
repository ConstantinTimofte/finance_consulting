package com.model.investment;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvestmentsOfClientsDto {

    private String firstName;
    private String lastName;
    private String investmentName;
    private Boolean statusOfPayment;/** Single investment*/
    private String expiringDate;
    private Integer remainingDays;
    private Integer mounth;/*Ogni quanto deve investire*/
    private Integer sum;


}
