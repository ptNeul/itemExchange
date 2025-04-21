package com.neul.itemexchange.dto.itemmetadata;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMetadataResponseDto {

  private Long itemId;
  private String itemName;
  private String image;
  private String detail;
}
