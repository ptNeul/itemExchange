package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.BaseException;
import lombok.Getter;

@Getter
public class UserException extends BaseException {

  public UserException(UserErrorCode errorCode) {
    super(errorCode);
  }
}
