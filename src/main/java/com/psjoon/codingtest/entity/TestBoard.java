package com.psjoon.codingtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestBoard")
@Entity
public class TestBoard {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tId;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false, length = 255)
    private String tTitle;

    @Column(nullable = false, length = 4000)
    private String tQuestion;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tWriteTime;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tUpdateTime;
}
