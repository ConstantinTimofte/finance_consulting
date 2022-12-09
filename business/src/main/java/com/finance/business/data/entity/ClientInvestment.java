package com.finance.business.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client_investment")
public class ClientInvestment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)

    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client idClient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_investment", nullable = false)
    private Investment idInvestment;

    @Column(name = "investment", nullable = false)
    private Integer investment;

    @Column(name = "mounth")
    private Integer mounth;

    @Column(name = "date_activation_invest")
    private LocalDate activationInvestment;

    @Column(name = "status_of_payment", nullable = false)
    private Boolean statusOfPayment;

}