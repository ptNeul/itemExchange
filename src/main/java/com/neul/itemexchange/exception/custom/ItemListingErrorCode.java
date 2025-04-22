package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ItemListingErrorCode implements ErrorCode {

  ITEM_LISTING_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 판매 목록이 존재하지 않습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "수정은 작성자만 가능합니다.");

  private final HttpStatus status;
  private final String message;

  ItemListingErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
