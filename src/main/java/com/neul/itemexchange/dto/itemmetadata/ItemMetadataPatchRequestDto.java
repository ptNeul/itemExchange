package com.neul.itemexchange.dto.itemmetadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemMetadataPatchRequestDto {

  private String itemName;
  private String imageUrl;
  private String detail;
}
