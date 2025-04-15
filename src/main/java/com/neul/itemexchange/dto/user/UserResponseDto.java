package com.neul.itemexchange.dto.user;

import com.neul.itemexchange.type.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

  private String username;
  private String email;
  private String nickname;
  private UserRole role;
  private Long balance;
}
