package com.psjoon.codingtest.entity;

import com.psjoon.codingtest.entity.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Member")
@Entity
public class Member implements UserDetails {

    @Id
    @Column(unique = true)
    private String id;

    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Integer phone;

    @Column(nullable = false)
    private Integer active = 0;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authority> authorities = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        if (this.active == null) {
            this.active = 0;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(auth -> new org.springframework.security.core.authority.SimpleGrantedAuthority(auth.getAuthorityName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active == 0;
    }
}
