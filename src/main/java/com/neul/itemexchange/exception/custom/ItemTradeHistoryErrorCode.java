package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemTradeHistoryErrorCode implements ErrorCode {

  TRADE_HISTORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "거래 내역이 존재하지 않습니다.");

  private final HttpStatus status;
  private final String message;
}
