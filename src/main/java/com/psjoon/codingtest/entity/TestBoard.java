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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_board_seq")
    @SequenceGenerator(name = "test_board_seq", sequenceName = "test_board_sequence", allocationSize = 1)
    private Integer tId;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false, length = 255)
    private String tTitle;

    @Column(nullable = false, length = 4000)
    private String tQuestion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tWriteTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tUpdateTime;
}
