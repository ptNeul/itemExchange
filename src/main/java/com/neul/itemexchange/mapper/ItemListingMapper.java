package com.neul.itemexchange.mapper;

import com.neul.itemexchange.domain.ItemListing;
import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.type.ItemListingStatus;
import org.springframework.stereotype.Component;

@Component
public class ItemListingMapper {

  public ItemListing toEntity(ItemListingRegisterRequestDto dto) {
    return ItemListing.builder()
        .price(dto.getPrice())
        .quantity(dto.getQuantity())
        .status(ItemListingStatus.SALE)
        .build();
  }

  public ItemListingResponseDto toDto(ItemListing entity) {
    return ItemListingResponseDto.builder()
        .listingId(entity.getListingId())
        .sellerUsername(entity.getSeller().getUsername())
        .itemId(entity.getItemMetadata().getItemId())
        .itemName(entity.getItemMetadata().getItemName())
        .itemImageUrl(entity.getItemMetadata().getImageUrl())
        .price(entity.getPrice())
        .quantity(entity.getQuantity())
        .status(entity.getStatus())
        .build();
  }
}
