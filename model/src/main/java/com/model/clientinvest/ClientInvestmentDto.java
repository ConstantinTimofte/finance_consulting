package com.model.clientinvest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInvestmentDto {

    private String firstName;
    private String secondName;
    @NotEmpty
    @NotNull
    private String investmentName;
    private Integer yearSalary;
    private Integer mounth;
    @Min(1)
    private Integer sumToInvest;
    private Boolean client;

    public ClientInvestmentDto(String firstName, String lastName, Integer mounth, Integer yearSalary) {
        this.firstName = firstName;
        this.secondName = lastName;
        this.mounth = mounth;
        this.yearSalary = yearSalary;
    }

    public ClientInvestmentDto(String firstName, String lastName, Integer mounth, Integer yearSalary, Integer sumToInvest, Boolean client) {
        this(firstName, lastName, mounth, yearSalary);
        this.sumToInvest = sumToInvest;
        this.client = client;
    }
}
