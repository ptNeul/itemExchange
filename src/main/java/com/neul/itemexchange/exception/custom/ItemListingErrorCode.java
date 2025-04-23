package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemListingErrorCode implements ErrorCode {

  ITEM_LISTING_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 판매 목록이 존재하지 않습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "수정은 작성자만 가능합니다."),
  CANNOT_MODIFY_COMPLETED_ITEM(HttpStatus.BAD_REQUEST, "거래 완료된 아이템은 수정할 수 없습니다."),
  INVALID_PURCHASE_QUANTITY(HttpStatus.BAD_REQUEST, "구매 수량은 1 이상이어야 합니다."),
  NOT_ENOUGH_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, "남은 수량이 부족합니다."),
  PRICE_RANGE_REQUIRED(HttpStatus.BAD_REQUEST, "최소 금액과 최대 금액을 입력해야 합니다."),
  INVALID_PRICE_RANGE(HttpStatus.BAD_REQUEST, "금액의 범위가 유효하지 않습니다.");

  private final HttpStatus status;
  private final String message;
}
