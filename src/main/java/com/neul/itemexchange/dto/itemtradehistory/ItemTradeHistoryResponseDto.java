package com.neul.itemexchange.dto.itemtradehistory;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemTradeHistoryResponseDto {

  private Long historyId;
  private Long listingId;
  private String buyerUsername;
  private int quantity;
  private Long totalPrice;
  private LocalDateTime createdAt;
}
