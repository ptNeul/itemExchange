package com.neul.itemexchange.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/sessions")
  public ResponseEntity<UserResponseDto> login(@RequestBody UserSimpleRequestDto dto,
      HttpSession session) {
    UserResponseDto result = userService.login(dto);
    session.setAttribute(
        SPRING_SECURITY_CONTEXT_KEY,
        SecurityContextHolder.getContext()
    );
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/sessions")
  public ResponseEntity<Void> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterRequestDto dto) {
    UserResponseDto result = userService.registerUser(dto);
    return ResponseEntity.status(CREATED).body(result);
  }

  @PatchMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponseDto> patchUser(@RequestBody UserPatchRequestDto dto,
      @AuthenticationPrincipal UserDetails userDetails) {
    UserResponseDto result = userService.patchUser(userDetails.getUsername(), dto);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteUser(userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/me/charge")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> charge(@RequestParam long amount,
      @AuthenticationPrincipal UserDetails userDetails) {
    userService.charge(userDetails.getUsername(), amount);
    return ResponseEntity.ok().build();
  }
}
