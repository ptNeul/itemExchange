package com.neul.itemexchange.dto.user;

import com.neul.itemexchange.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDto {

  private String username;
  private String password;
  private String email;
  private String nickname;
  private UserRole role;

  // Seller
  private String accountNumber;
  private String businessNumber;
}
