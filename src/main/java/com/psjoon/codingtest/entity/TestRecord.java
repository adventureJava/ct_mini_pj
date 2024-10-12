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
@Table(name = "TestRecord")
@Entity
public class TestRecord {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer record_id;

    @ManyToOne  // 여러 권한이 하나의 사용자와 연결됨
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @ManyToOne  // 여러 권한이 하나의 사용자와 연결됨
    @JoinColumn(name = "tId", referencedColumnName = "tId")
    private TestBoard testBoard;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tWriteTime;
//
//    public void setMember(Member member) {
//        this.member = member;
//    }
//    public void setTestBoard(TestBoard testBoard) {
//        this.testBoard = testBoard;
//    }
}
