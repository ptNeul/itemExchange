package com.neul.itemexchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .requestMatchers("/admin/register", "/user/login", "/user/logout", "/user/register").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자
            .requestMatchers("/seller/**").hasRole("SELLER") // 판매자
            .requestMatchers("/buyer/**").hasRole("BUYER") // 구매자
            .requestMatchers("/", "/register", "/login").permitAll() // 누구나
            .anyRequest().authenticated()
        )
        .formLogin(AbstractHttpConfigurer::disable)
//        .formLogin(form -> form
//            .loginPage("/login") // 사용자 정의 로그인 페이지
//            .loginProcessingUrl("/login") // 로그인 폼 전송시 이 URL로 POST요청
//            .defaultSuccessUrl("/", true) // 로그인 성공시 이동
//            .failureUrl("/login?error=true") // 로그인 실패시 이동
//            .permitAll()
//        )
        .logout(logout -> logout
            .logoutUrl("/logout") // 로그아웃 시 이동
            .logoutSuccessUrl("/") // 로그아웃 후 이동
            .invalidateHttpSession(true) // 세션 무효화
            .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
