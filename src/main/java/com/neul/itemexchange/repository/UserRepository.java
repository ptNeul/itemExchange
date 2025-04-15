package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.type.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  // 관리자 등록 확인
  boolean existsByRole(UserRole role);

  // 이메일 중복 확인
  boolean existsByEmail(String email);

  // 닉네임 중복 확인
  boolean existsByNickname(String nickname);
}
