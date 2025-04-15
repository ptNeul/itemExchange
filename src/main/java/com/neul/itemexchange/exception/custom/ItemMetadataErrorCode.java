package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ItemMetadataErrorCode implements ErrorCode {

  DUPLICATE_ITEM_NAME(HttpStatus.BAD_REQUEST, "같은 이름의 아이템이 이미 존재합니다."),
  DUPLICATE_IMAGE(HttpStatus.BAD_REQUEST, "같은 이미지의 아이템이 이미 존재합니다."),
  ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 아이템이 존재하지 않습니다.");

  private final HttpStatus status;
  private final String message;

  ItemMetadataErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
