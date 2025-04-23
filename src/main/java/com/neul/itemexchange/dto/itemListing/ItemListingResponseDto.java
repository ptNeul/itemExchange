package com.neul.itemexchange.dto.itemListing;

import com.neul.itemexchange.type.ItemListingStatus;
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
public class ItemListingResponseDto {

  private Long listingId;
  private String sellerUsername;
  private Long itemId;
  private String itemName;
  private String itemImageUrl;
  private int price;
  private int quantity;
  private ItemListingStatus status;
}
