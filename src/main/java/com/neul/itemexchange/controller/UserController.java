package com.neul.itemexchange.controller;

import static org.springframework.http.HttpStatus.*;

import com.neul.itemexchange.dto.user.UserPatchRequestDto;
import com.neul.itemexchange.dto.user.UserRegisterRequestDto;
import com.neul.itemexchange.dto.user.UserResponseDto;
import com.neul.itemexchange.dto.user.UserSimpleRequestDto;
import com.neul.itemexchange.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> login(@RequestBody UserSimpleRequestDto dto,
      HttpSession session) {
    UserResponseDto result = userService.login(dto, session);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterRequestDto dto) {
    UserResponseDto result = userService.registerUser(dto);
    return ResponseEntity.status(CREATED).body(result);
  }

  @PatchMapping("/patch")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponseDto> patchUser(@RequestBody UserPatchRequestDto dto,
      @AuthenticationPrincipal UserDetails userDetails) {
    UserResponseDto result = userService.patchUser(userDetails.getUsername(), dto);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("isAuthenticated")
  public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteUser(userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/charge")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> charge(@RequestParam long amount,
      @AuthenticationPrincipal UserDetails userDetails) {
    userService.charge(userDetails.getUsername(), amount);
    return ResponseEntity.ok().build();
  }
}
