package com.neul.itemexchange.dto.itemListing;

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
public class ItemListingRegisterRequestDto {
  private Long itemId;
  private int price;
  private int quantity;
}
