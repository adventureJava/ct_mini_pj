package com.psjoon.codingtest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "Payment")
@Entity
public class Payment {
    @Id
    @Column(unique = true)
    String MerchantUid;
}
