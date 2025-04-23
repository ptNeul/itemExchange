package com.neul.itemexchange.dto.itemmetadata;

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
public class ItemMetadataResponseDto {

  private Long itemId;
  private String itemName;
  private String imageUrl;
  private String detail;
}
