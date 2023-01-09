package com.model.clientinvest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchInvestmentDto {
    private String firstName;
    private String lastName;
    private String status;
    private String investmentName;
}
