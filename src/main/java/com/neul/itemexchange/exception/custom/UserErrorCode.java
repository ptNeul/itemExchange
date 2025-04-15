package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
  ALREADY_CREATED_ADMIN(HttpStatus.BAD_REQUEST, "관리자 계정은 이미 존재합니다."),
  DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
  MISSING_SELLER_INFO(HttpStatus.BAD_REQUEST, "계좌 번호와 사업자 등록번호를 입력해야 합니다."),
  INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "충전금액은 0보다 커야 합니다."),
  ONLY_BUYER_CAN_CHARGE(HttpStatus.BAD_REQUEST, "충전은 구매자만 가능합니다."),
  INVALID_TRANSFER_AMOUNT(HttpStatus.BAD_REQUEST, "금액이 0보다 커야 합니다."),
  INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다.");

  private final HttpStatus status;
  private final String message;

  UserErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
