package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.itemtradehistory.ItemTradeHistoryResponseDto;
import com.neul.itemexchange.service.ItemTradeHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-trade-histories")
public class ItemTradeHistoryController {

  private final ItemTradeHistoryService itemTradeHistoryService;

  /**
   * 전체 조회
   */
  @GetMapping
  public ResponseEntity<List<ItemTradeHistoryResponseDto>> getAllHistories() {
    List<ItemTradeHistoryResponseDto> result = itemTradeHistoryService.getAllHistories();
    return ResponseEntity.ok(result);
  }

  /**
   * 로그인한 유저의 거래 내역 조회
   */
  @GetMapping("/me")
  public ResponseEntity<List<ItemTradeHistoryResponseDto>> getMyHistories(
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    String username = userDetails.getUsername();
    List<ItemTradeHistoryResponseDto> result = itemTradeHistoryService.getMyHistories(username);
    return ResponseEntity.ok(result);
  }
}
