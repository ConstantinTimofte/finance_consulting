package com.model.clientinvest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInvestmentDto {

    private String firstName;
    private String secondName;
    private String investmentName;
    private Integer mounth;
    private Integer sumToInvest;
}
