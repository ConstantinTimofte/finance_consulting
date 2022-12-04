package com.finance.business.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "investment")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 10485760)
    private String description;

    @OneToMany(mappedBy = "idInvestment", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ClientInvestment> clientInvestments = new ArrayList<>();



    public Investment(String name, String description) {
        this.name = name;
        this.description = description;
    }
}