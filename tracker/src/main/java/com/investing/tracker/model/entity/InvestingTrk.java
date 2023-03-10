package com.investing.tracker.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "investing_trk", indexes = {
        @Index(name = "investing_trk_first_name_day_pay_index", columnList = "first_name, day_pay")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class InvestingTrk {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;


    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "investing_name", nullable = false)
    private String investingName;

    @Column(name = "day_pay", nullable = false)
    private LocalDate dayPay;

    @Column(name = "sum")
    private Integer sum;

}