package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.user.UserErrorCode.ALREADY_CREATED_ADMIN;
import static com.neul.itemexchange.exception.user.UserErrorCode.INSUFFICIENT_BALANCE;
import static com.neul.itemexchange.exception.user.UserErrorCode.INVALID_CHARGE_AMOUNT;
import static com.neul.itemexchange.exception.user.UserErrorCode.INVALID_PASSWORD;
import static com.neul.itemexchange.exception.user.UserErrorCode.INVALID_TRANSFER_AMOUNT;
import static com.neul.itemexchange.exception.user.UserErrorCode.MISSING_SELLER_INFO;
import static com.neul.itemexchange.exception.user.UserErrorCode.ONLY_BUYER_CAN_CHARGE;
import static com.neul.itemexchange.exception.user.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.UserRole.ADMIN;
import static com.neul.itemexchange.type.UserRole.BUYER;
import static com.neul.itemexchange.type.UserRole.SELLER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neul.itemexchange.domain.Seller;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.user.UserPatchRequestDto;
import com.neul.itemexchange.dto.user.UserRegisterRequestDto;
import com.neul.itemexchange.dto.user.UserResponseDto;
import com.neul.itemexchange.dto.user.UserSimpleRequestDto;
import com.neul.itemexchange.exception.user.UserException;
import com.neul.itemexchange.mapper.UserMapper;
import com.neul.itemexchange.repository.SellerRepository;
import com.neul.itemexchange.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SellerRepository sellerRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserMapper userMapper;

  @Mock
  private HttpSession session;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void registerAdmin() {
    // given
    UserSimpleRequestDto request = new UserSimpleRequestDto("admin", "pass");
    User userEntity = User.builder()
        .username("admin")
        .password("encodedPw")
        .email("admin@system")
        .nickname("관리자")
        .role(ADMIN)
        .balance(0L)
        .build();
    UserResponseDto response = UserResponseDto.builder()
        .username("admin")
        .email("admin@system")
        .nickname("관리자")
        .role(ADMIN)
        .balance(0L)
        .build();

    when(userRepository.existsByRole(ADMIN)).thenReturn(false);
    when(userMapper.toAdminEntity(request)).thenReturn(userEntity);
    when(passwordEncoder.encode("1234")).thenReturn("encodedPw");
    when(userRepository.save(userEntity)).thenReturn(userEntity);
    when(userMapper.toDto(userEntity)).thenReturn(response);

    // when
    UserResponseDto result = userService.registerAdmin(request);

    // then
    assertThat(result.getUsername()).isEqualTo("admin");
    assertThat(result.getRole()).isEqualTo(ADMIN);
    verify(userRepository).save(userEntity);
  }

  @Test
  void registerAdmin_alreadyCreatedAdmin() {
    // given
    UserSimpleRequestDto dto = new UserSimpleRequestDto("admin", "pass");

    when(userRepository.existsByRole(ADMIN)).thenReturn(true);

    // when
    // then
    assertThatThrownBy(() -> userService.registerAdmin(dto))
        .isInstanceOf(UserException.class)
        .hasMessage(ALREADY_CREATED_ADMIN.getMessage());

    verify(userRepository, never()).save(any());
  }

  @Test
  void login() {
    // given
    UserSimpleRequestDto request = new UserSimpleRequestDto("admin", "pass");
    User userEntity = User.builder()
        .username("admin")
        .password("encodedPw")
        .email("admin@system")
        .nickname("관리자")
        .role(ADMIN)
        .balance(0L)
        .build();
    UserResponseDto response = UserResponseDto.builder()
        .username("admin")
        .email("admin@system")
        .nickname("관리자")
        .role(ADMIN)
        .balance(0L)
        .build();

    when(userRepository.findById("admin")).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches("pass", "encodedPw")).thenReturn(true);
    when(userMapper.toDto(userEntity)).thenReturn(response);

    // when
    UserResponseDto result = userService.login(request, session);

    // then
    assertThat(result.getUsername()).isEqualTo("admin");
    verify(session).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
  }

  @Test
  void login_userNotFound() {
    // given
    UserSimpleRequestDto dto = new UserSimpleRequestDto("janna", "1234");
    when(userRepository.findById("janna")).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> userService.login(dto, session))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());

    verify(session, never()).setAttribute(any(), any());
  }

  @Test
  void login_invalidPassword() {
    // given
    UserSimpleRequestDto dto = new UserSimpleRequestDto("janna", "1234");

    User user = User.builder()
        .username("janna")
        .password("encodedPw")
        .build();

    when(userRepository.findById("janna")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("1234", "encodedPw")).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> userService.login(dto, session))
        .isInstanceOf(UserException.class)
        .hasMessage(INVALID_PASSWORD.getMessage());

    verify(session, never()).setAttribute(any(), any());
  }

  @Test
  void registerUser() {
    // given
    UserRegisterRequestDto dto = UserRegisterRequestDto.builder()
        .username("buyer1")
        .password("1234")
        .email("buyer1@test.com")
        .nickname("바이어1")
        .role(BUYER)
        .build();

    when(userRepository.existsById(dto.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.existsByNickname(dto.getNickname())).thenReturn(false);
    when(passwordEncoder.encode("1234")).thenReturn("encodedPw");

    User user = User.builder()
        .username(dto.getUsername())
        .password("encodedPw")
        .email(dto.getEmail())
        .nickname(dto.getNickname())
        .role(BUYER)
        .balance(0L)
        .build();

    UserResponseDto response = UserResponseDto.builder()
        .username("buyer1")
        .email("buyer1@test.com")
        .nickname("바이어1")
        .role(BUYER)
        .balance(0L)
        .build();

    when(userMapper.toRegisterEntity(dto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(response);

    // when
    UserResponseDto result = userService.registerUser(dto);

    // then
    assertThat(result.getUsername()).isEqualTo("buyer1");
    assertThat(result.getRole()).isEqualTo(BUYER);
    verify(userRepository).save(user);
  }

  @Test
  void registerSeller() {
    // given
    UserRegisterRequestDto dto = UserRegisterRequestDto.builder()
        .username("seller1")
        .password("1234")
        .email("seller1@test.com")
        .nickname("셀러1")
        .role(SELLER)
        .accountNumber("123-456-789")
        .businessNumber("123-456-789")
        .build();

    when(userRepository.existsById(dto.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.existsByNickname(dto.getNickname())).thenReturn(false);
    when(passwordEncoder.encode("1234")).thenReturn("encodedPw");

    User user = User.builder()
        .username(dto.getUsername())
        .password("encodedPw")
        .email(dto.getEmail())
        .nickname(dto.getNickname())
        .role(SELLER)
        .balance(0L)
        .build();

    UserResponseDto response = UserResponseDto.builder()
        .username("seller1")
        .email("seller1@test.com")
        .nickname("셀러1")
        .role(SELLER)
        .balance(0L)
        .build();

    when(userMapper.toRegisterEntity(dto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(response);

    Seller seller = new Seller();
    seller.setUser(user);
    seller.setAccountNumber(dto.getAccountNumber());
    seller.setBusinessNumber(dto.getBusinessNumber());
    when(userMapper.toSellerEntity(dto, user)).thenReturn(seller);

    // when
    UserResponseDto result = userService.registerUser(dto);

    // then
    assertThat(result.getUsername()).isEqualTo("seller1");
    assertThat(result.getRole()).isEqualTo(SELLER);
    verify(userRepository).save(user);
    verify(sellerRepository).save(seller);
  }

  @Test
  void registerSeller_missingSellerInfo() {
    // given
    UserRegisterRequestDto dto = UserRegisterRequestDto.builder()
        .username("seller1")
        .password("1234")
        .email("seller1@test.com")
        .nickname("셀러1")
        .role(SELLER)
        .build();

    when(userRepository.existsById(dto.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.existsByNickname(dto.getNickname())).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> userService.registerUser(dto))
        .isInstanceOf(UserException.class)
        .hasMessage(MISSING_SELLER_INFO.getMessage());

    verify(userRepository, never()).save(any());
    verify(sellerRepository, never()).save(any());
  }

  @Test
  void patchBuyer() {
    // given
    String username = "buyer1";

    UserPatchRequestDto dto = UserPatchRequestDto.builder()
        .password("newPw")
        .email("newbuyer@email.com")
        .nickname("바뀐바이어")
        .build();

    User user = User.builder()
        .username(username)
        .password("oldEncodedPw")
        .email("old@email.com")
        .nickname("옛날바이어")
        .role(BUYER)
        .balance(0L)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("newPw")).thenReturn("newEncodedPw");

    UserResponseDto response = UserResponseDto.builder()
        .username(username)
        .email("newbuyer@email.com")
        .nickname("바뀐바이어")
        .role(BUYER)
        .balance(0L)
        .build();

    when(userMapper.toDto(user)).thenReturn(response);

    // when
    UserResponseDto result = userService.patchUser(username, dto);

    // then
    assertThat(result.getEmail()).isEqualTo("newbuyer@email.com");
    assertThat(result.getNickname()).isEqualTo("바뀐바이어");
    assertThat(user.getPassword()).isEqualTo("newEncodedPw");
    verify(userRepository).findById(username);
  }

  @Test
  void patchSeller() {
    // given
    String username = "seller1";

    UserPatchRequestDto dto = UserPatchRequestDto.builder()
        .password("newPw")
        .email("updated@seller.com")
        .nickname("수정판매자")
        .accountNumber("987-654-321")
        .businessNumber("987-654-321")
        .build();

    User user = User.builder()
        .username(username)
        .password("oldEncodedPw")
        .email("old@seller.com")
        .nickname("판매자")
        .role(SELLER)
        .balance(0L)
        .build();

    Seller seller = new Seller();
    seller.setUser(user);
    seller.setAccountNumber("111-222-333");
    seller.setBusinessNumber("111-222-333");

    when(userRepository.findById(username)).thenReturn(Optional.of(user));
    when(sellerRepository.findById(username)).thenReturn(Optional.of(seller));
    when(passwordEncoder.encode("newPw")).thenReturn("new EncodedPw");

    UserResponseDto response = UserResponseDto.builder()
        .username(username)
        .email("updated@seller.com")
        .nickname("수정판매자")
        .role(BUYER)
        .balance(0L)
        .build();

    when(userMapper.toDto(user)).thenReturn(response);

    // when
    UserResponseDto result = userService.patchUser(username, dto);

    // then
    assertThat(result.getEmail()).isEqualTo("updated@seller.com");
    assertThat(seller.getAccountNumber()).isEqualTo("987-654-321");
    assertThat(seller.getBusinessNumber()).isEqualTo("987-654-321");
    verify(sellerRepository).findById(username);
  }

  @Test
  void patch_userNotFound() {
    // given
    String username = "ghost";
    when(userRepository.findById(username)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> userService.patchUser(username, new UserPatchRequestDto()))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());
  }

  @Test
  void deleteBuyer() {
    // given
    String username = "buyer1";
    User user = User.builder()
        .username(username)
        .password("pw")
        .email("buyer@test.com")
        .nickname("바이어")
        .role(BUYER)
        .balance(0L)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));

    // when
    userService.deleteUser(username);

    // then
    verify(userRepository).delete(user);
    verify(sellerRepository, never()).deleteById(any());
  }

  @Test
  void deleteSeller() {
    // given
    String username = "seller1";
    User user = User.builder()
        .username(username)
        .password("pw")
        .email("seller@test.com")
        .nickname("판매자")
        .role(SELLER)
        .balance(0L)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));

    // when
    userService.deleteUser(username);

    // then
    verify(userRepository).delete(user);
    verify(sellerRepository).deleteById(username);
  }

  @Test
  void delete_userNotFound() {
    // given
    String username = "ghost";
    when(userRepository.findById(username)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> userService.deleteUser(username))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());

    verify(userRepository, never()).delete(any());
    verify(sellerRepository, never()).deleteById(any());
  }

  @Test
  void charge() {
    // given
    String username = "buyer1";
    User user = User.builder()
        .username(username)
        .balance(1000L)
        .role(BUYER)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));

    // when
    userService.charge(username, 5000L);

    // then
    assertThat(user.getBalance()).isEqualTo(6000L);
    verify(userRepository).findById(username);
  }

  @Test
  void charge_notFoundUser() {
    // given
    when(userRepository.findById("ghost")).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> userService.charge("ghost", 3000L))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());
  }

  @Test
  void charge_invalidChargeAmount() {
    // when
    // then
    assertThatThrownBy(() -> userService.charge("buyer1", -1000L))
        .isInstanceOf(UserException.class)
        .hasMessage(INVALID_CHARGE_AMOUNT.getMessage());
  }

  @Test
  void charge_onlyBuyerCanCharge() {
    // given
    String username = "seller1";
    User user = User.builder()
        .username(username)
        .balance(1000L)
        .role(SELLER)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));

    // when
    // then
    assertThatThrownBy(() -> userService.charge(username, 5000L))
        .isInstanceOf(UserException.class)
        .hasMessage(ONLY_BUYER_CAN_CHARGE.getMessage());
  }

  @Test
  void transferBalance() {
    // given
    String buyerUsername = "buyer1";
    String sellerUsername = "seller1";

    User buyer = User.builder()
        .username(buyerUsername)
        .balance(10_000L)
        .role(BUYER)
        .build();

    User seller = User.builder()
        .username(sellerUsername)
        .balance(5_000L)
        .role(SELLER)
        .build();

    when(userRepository.findById(buyerUsername)).thenReturn(Optional.of(buyer));
    when(userRepository.findById(sellerUsername)).thenReturn(Optional.of(seller));

    // when
    userService.transferBalance(buyerUsername, sellerUsername, 3000L);

    // then
    assertThat(buyer.getBalance()).isEqualTo(7000L);
    assertThat(seller.getBalance()).isEqualTo(8000L);
  }

  @Test
  void transferBalance_insufficientBalance() {
    // given
    String buyerUsername = "buyer1";
    String sellerUsername = "seller1";

    User buyer = User.builder()
        .username(buyerUsername)
        .balance(1000L)
        .role(BUYER)
        .build();

    User seller = User.builder()
        .username(sellerUsername)
        .balance(2000L)
        .role(SELLER)
        .build();

    when(userRepository.findById(buyerUsername)).thenReturn(Optional.of(buyer));
    when(userRepository.findById(sellerUsername)).thenReturn(Optional.of(seller));

    // when
    // then
    assertThatThrownBy(() -> userService.transferBalance(buyerUsername, sellerUsername, 5000L))
        .isInstanceOf(UserException.class)
        .hasMessage(INSUFFICIENT_BALANCE.getMessage());
  }

  @Test
  void transferBalance_invalidTransferAmount() {
    assertThatThrownBy(() -> userService.transferBalance("buyer1", "seller1", -100L))
        .isInstanceOf(UserException.class)
        .hasMessage(INVALID_TRANSFER_AMOUNT.getMessage());
  }
}