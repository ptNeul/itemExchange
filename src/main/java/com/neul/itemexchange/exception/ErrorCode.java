package com.neul.itemexchange.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode{

  HttpStatus getStatus();

  String getMessage();
}
