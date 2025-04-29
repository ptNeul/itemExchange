package com.neul.itemexchange.mapper;

import com.neul.itemexchange.domain.ItemTradeHistory;
import com.neul.itemexchange.dto.itemtradehistory.ItemTradeHistoryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ItemTradeHistoryMapper {

  public ItemTradeHistoryResponseDto toDto(ItemTradeHistory entity) {
    return ItemTradeHistoryResponseDto.builder()
        .historyId(entity.getHistoryId())
        .listingId(entity.getItemListing().getListingId())
        .buyerUsername(entity.getBuyer().getUsername())
        .quantity(entity.getQuantity())
        .totalPrice(entity.getTotalPrice())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
