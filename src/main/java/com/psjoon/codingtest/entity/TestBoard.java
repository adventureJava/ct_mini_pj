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
    private String tId;

    @Column(nullable = false, length = 255)
    private String tTitle;

    @Column(nullable = false, length = 4000)
    private String tQuestion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tWriteTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tUpdateTime;
}
