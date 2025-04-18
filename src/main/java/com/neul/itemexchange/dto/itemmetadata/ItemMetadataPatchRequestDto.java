package com.neul.itemexchange.dto.itemmetadata;

import lombok.*;

@Getter
@Setter
public class ItemMetadataPatchRequestDto {
  private String itemName;
  private String image;
  private String detail;
}
