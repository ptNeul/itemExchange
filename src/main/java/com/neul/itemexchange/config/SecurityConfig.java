package com.neul.itemexchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  // SecurityFilterChain : 보안 규칙을 담은 필터 체인 객체
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 기능 끄기
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/admins",
                "/users",
                "/users/sessions",
                "/users/register",
                "/item-listings/**"
            ).permitAll()
            .requestMatchers("/admins/**").hasRole("ADMIN") // 관리자
            .requestMatchers("/sellers/**").hasRole("SELLER") // 판매자
            .requestMatchers("/buyers/**").hasRole("BUYER") // 구매자
            .anyRequest().authenticated()
        )
        .formLogin(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
