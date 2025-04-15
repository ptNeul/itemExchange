package com.neul.itemexchange.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSimpleRequestDto {

  private String username;
  private String password;
}
