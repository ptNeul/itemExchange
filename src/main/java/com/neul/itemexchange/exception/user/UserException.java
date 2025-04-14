package com.neul.itemexchange.exception.user;

import com.neul.itemexchange.exception.BaseException;
import lombok.Getter;

@Getter
public class UserException extends BaseException {

  private final UserErrorCode errorCode;

  public UserException(UserErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
