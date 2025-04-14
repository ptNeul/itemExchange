package com.neul.itemexchange.dto.user;

import lombok.*;

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
