package com.psjoon.codingtest.config;

import com.psjoon.codingtest.config.jwtFilter.JwtTokenFilter;
import com.psjoon.codingtest.config.jwtFilter.JwtTokenProvider;
import com.psjoon.codingtest.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/join","/member/join","/", "/login").permitAll()
//                        .anyRequest().authenticated())
                        .anyRequest().permitAll()) // 모든 요청을 허용
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/mymenu"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // AuthenticationManager는 직접 설정하지 않음. Spring Security가 처리
                .userDetailsService(customUserDetailsService);
        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


