package com.psjoon.codingtest.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Authority")
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
    @SequenceGenerator(name = "authority_seq", sequenceName = "authority_sequence", allocationSize = 1)
    private Integer authority_id;

    @ManyToOne  // 여러 권한이 하나의 사용자와 연결됨
    @JoinColumn(name = "id", referencedColumnName = "id")  // Member의 id를 참조하는 외래키
    private Member member;  // Member 엔터티와의 연관관계 설정

    @Column(nullable = false)
    private String authorityName = "ROLE_USER";

    public String getAuthorityName() {
        return authorityName;  // 권한 이름을 반환하는 메서드
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
