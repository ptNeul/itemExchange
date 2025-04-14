package com.neul.itemexchange.mapper;

import com.neul.itemexchange.domain.Seller;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.user.UserRegisterRequestDto;
import com.neul.itemexchange.dto.user.UserResponseDto;
import com.neul.itemexchange.dto.user.UserSimpleRequestDto;
import com.neul.itemexchange.type.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponseDto toDto(User user) {
    return UserResponseDto.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .role(user.getRole())
        .balance(user.getBalance())
        .build();
  }

  public User toAdminEntity(UserSimpleRequestDto dto) {
    return User.builder()
        .username(dto.getUsername())
        .password(dto.getPassword())
        .email("admin@system")
        .nickname("관리자")
        .role(UserRole.ADMIN)
        .balance(0L)
        .build();
  }

  public User toRegisterEntity(UserRegisterRequestDto dto) {
    return User.builder()
        .username(dto.getUsername())
        .password(dto.getPassword())
        .email(dto.getEmail())
        .nickname(dto.getNickname())
        .role(dto.getRole())
        .balance(0L)
        .build();
  }

  public Seller toSellerEntity(UserRegisterRequestDto dto, User user) {
    Seller seller = new Seller();
    seller.setUser(user);
    seller.setAccountNumber(dto.getAccountNumber());
    seller.setBusinessNumber(dto.getBusinessNumber());

    return seller;
  }
}
