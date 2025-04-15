package com.neul.itemexchange.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<?> handleBaseException(BaseException ex) {
    ErrorCode errorCode = ex.getErrorCode();

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(Map.of(
            "code", errorCode.getClass().getSimpleName() + "." + errorCode.toString(),
            "message", errorCode.getMessage()
        ));
  }
}
