package com.psjoon.codingtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OrderProduct")
@Entity
public class OrderProduct {
    @Id
    @Column(unique = true)
    String merchantUid;

    @ManyToOne  // 여러 권한이 하나의 사용자와 연결됨
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @Column(nullable = false)
    String impUid;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    String payMethod;

    @Column(nullable = false)
    String status;

}


