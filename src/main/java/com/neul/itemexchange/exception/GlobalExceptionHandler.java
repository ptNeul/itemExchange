package com.neul.itemexchange.exception;

import com.neul.itemexchange.exception.user.UserException;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public ResponseEntity<?> handleUserException(UserException ex) {
    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(Map.of(
            "code", ex.getErrorCode().name(),
            "message", ex.getErrorCode().getMessage()
        ));
  }
}
