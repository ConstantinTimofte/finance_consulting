package com.finance.business.data.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {
    @Id
    //https://stackoverflow.com/questions/32719662/generationtype-sequence-does-not-generate-sequence-in-hibernate
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "client", nullable = false)
    private Boolean client = false;

    @Column(name = "payment")
    private Boolean payment;

    @Column(name = "year_salary")
    private Integer yearSalary;

    @Column(name = "email_adress")
    private String emailAdress;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "notes", length = 10485760)
    private String notes;

    @OneToMany(mappedBy = "idClient", fetch = FetchType.LAZY)
    private Set<ClientInvestment> clientInvestments = new LinkedHashSet<>();

}