package com.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private Boolean client;
    private Boolean payment;
    private Integer yearSalary;
    @NotEmpty
    private String emailAdress;
    private String phoneNumber;
    private String city;
    private String cnp;
    private String notes;
}