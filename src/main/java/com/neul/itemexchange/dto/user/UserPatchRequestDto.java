package com.neul.itemexchange.dto.user;

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
public class UserPatchRequestDto {

  private String password;
  private String email;
  private String nickname;

  // Seller
  private String accountNumber;
  private String businessNumber;
}
