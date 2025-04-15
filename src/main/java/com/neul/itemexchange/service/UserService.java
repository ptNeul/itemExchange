package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.UserErrorCode.ALREADY_CREATED_ADMIN;
import static com.neul.itemexchange.exception.custom.UserErrorCode.DUPLICATE_EMAIL;
import static com.neul.itemexchange.exception.custom.UserErrorCode.DUPLICATE_NICKNAME;
import static com.neul.itemexchange.exception.custom.UserErrorCode.DUPLICATE_USERNAME;
import static com.neul.itemexchange.exception.custom.UserErrorCode.INSUFFICIENT_BALANCE;
import static com.neul.itemexchange.exception.custom.UserErrorCode.INVALID_CHARGE_AMOUNT;
import static com.neul.itemexchange.exception.custom.UserErrorCode.INVALID_TRANSFER_AMOUNT;
import static com.neul.itemexchange.exception.custom.UserErrorCode.MISSING_SELLER_INFO;
import static com.neul.itemexchange.exception.custom.UserErrorCode.ONLY_BUYER_CAN_CHARGE;
import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.UserRole.ADMIN;
import static com.neul.itemexchange.type.UserRole.BUYER;
import static com.neul.itemexchange.type.UserRole.SELLER;

import com.neul.itemexchange.domain.Seller;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.user.UserPatchRequestDto;
import com.neul.itemexchange.dto.user.UserRegisterRequestDto;
import com.neul.itemexchange.dto.user.UserResponseDto;
import com.neul.itemexchange.dto.user.UserSimpleRequestDto;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.UserMapper;
import com.neul.itemexchange.repository.SellerRepository;
import com.neul.itemexchange.repository.UserRepository;
import com.neul.itemexchange.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final SellerRepository sellerRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;

  /**
   * 어드민 계정 등록
   */
  public UserResponseDto registerAdmin(UserSimpleRequestDto dto) {

    // 이미 어드민이 존재하면 예외
    if (userRepository.existsByRole(ADMIN)) {
      throw new UserException(ALREADY_CREATED_ADMIN);
    }

    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    User user = userMapper.toAdminEntity(dto);

    return userMapper.toDto(userRepository.save(user));
  }

  /**
   * 로그인
   */
  public UserResponseDto login(UserSimpleRequestDto dto) {
    System.out.println(">>> UserService.login() 진입");

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
    );

    System.out.println(">>> 인증 완료");

    SecurityContextHolder.getContext().setAuthentication(authentication);
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    return userMapper.toDto(userDetails.getUser());
  }

  /**
   * 회원가입
   */
  public UserResponseDto registerUser(UserRegisterRequestDto dto) {

    if (userRepository.existsById(dto.getUsername())) {
      throw new UserException(DUPLICATE_USERNAME);
    }

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new UserException(DUPLICATE_EMAIL);
    }

    if (userRepository.existsByNickname(dto.getNickname())) {
      throw new UserException(DUPLICATE_NICKNAME);
    }

    if (dto.getRole() == SELLER) {
      if (dto.getAccountNumber() == null || dto.getBusinessNumber() == null) {
        throw new UserException(MISSING_SELLER_INFO);
      }
    }

    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    User user = userMapper.toRegisterEntity(dto);
    user = userRepository.save(user);

    if (dto.getRole() == SELLER) {
      Seller seller = userMapper.toSellerEntity(dto, user);
      sellerRepository.save(seller);
    }

    return userMapper.toDto(user);
  }

  /**
   * 화원정보 수정
   */
  public UserResponseDto patchUser(String username, UserPatchRequestDto dto) {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (dto.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
    if (dto.getEmail() != null) {
      user.setEmail(dto.getEmail());
    }
    if (dto.getNickname() != null) {
      user.setNickname(dto.getNickname());
    }

    if (user.getRole() == SELLER) {
      Seller seller = sellerRepository.findById(username)
          .orElseThrow(() -> new UserException(USER_NOT_FOUND));

      if (dto.getAccountNumber() != null) {
        seller.setAccountNumber(dto.getAccountNumber());
      }
      if (dto.getBusinessNumber() != null) {
        seller.setBusinessNumber(dto.getBusinessNumber());
      }
    }

    return userMapper.toDto(user);
  }

  /**
   * 회원 삭제
   */
  public void deleteUser(String username) {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (user.getRole() == SELLER) {
      sellerRepository.deleteById(username);
    }

    userRepository.delete(user);
  }

  /**
   * 잔액 충전
   */
  public void charge(String username, long amount) {
    if (amount < 0) {
      throw new UserException(INVALID_CHARGE_AMOUNT);
    }

    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (user.getRole() != BUYER) {
      throw new UserException(ONLY_BUYER_CAN_CHARGE);
    }

    user.setBalance(user.getBalance() + amount);
  }

  /**
   * 구매 발생 시 buyer → seller 잔액 이동 아직 구매 시스템이 없으므로 외부 호출 X
   */
  public void transferBalance(String buyerUsername, String sellerUsername, long amount) {
    if (amount <= 0) {
      throw new UserException(INVALID_TRANSFER_AMOUNT);
    }

    User buyer = userRepository.findById(buyerUsername)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    User seller = userRepository.findById(sellerUsername)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (buyer.getBalance() < amount) {
      throw new UserException((INSUFFICIENT_BALANCE));
    }

    buyer.setBalance(buyer.getBalance() - amount);
    seller.setBalance(seller.getBalance() + amount);
  }
}
