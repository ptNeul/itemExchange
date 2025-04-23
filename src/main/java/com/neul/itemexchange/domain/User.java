package com.neul.itemexchange.domain;

import com.neul.itemexchange.type.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 어드민 계정 등록
 * 회원가입
 * 로그인
 * 회원정보 수정
 * 탈퇴
 *
 * Buyer ↔ Seller balance logic
 */

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @Column(length = 50, nullable = false)
  private String username;

  @Column(length = 255, nullable = false)
  private String password;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 50, nullable = false, unique = true)
  private String nickname;

  @Builder.Default
  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private UserRole role = UserRole.BUYER;

  @Builder.Default
  @Column(nullable = false)
  private long balance = 0L;
}
