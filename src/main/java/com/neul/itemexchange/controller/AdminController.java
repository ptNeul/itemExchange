package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.user.UserResponseDto;
import com.neul.itemexchange.dto.user.UserSimpleRequestDto;
import com.neul.itemexchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponseDto> registerAdmin(@RequestBody UserSimpleRequestDto dto) {
    UserResponseDto result = userService.registerAdmin(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }
}
