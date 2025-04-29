package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.UserRole.BUYER;
import static com.neul.itemexchange.type.UserRole.SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neul.itemexchange.domain.ItemTradeHistory;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.itemtradehistory.ItemTradeHistoryResponseDto;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.ItemTradeHistoryMapper;
import com.neul.itemexchange.repository.ItemTradeHistoryRepository;
import com.neul.itemexchange.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemTradeHistoryServiceTest {

  @InjectMocks
  private ItemTradeHistoryService itemTradeHistoryService;

  @Mock
  private ItemTradeHistoryRepository itemTradeHistoryRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ItemTradeHistoryMapper itemTradeHistoryMapper;

  @Test
  void getAllHistories() {
    // given
    List<ItemTradeHistory> entityList = List.of(mock(ItemTradeHistory.class));
    List<ItemTradeHistoryResponseDto> dtoList = List.of(mock(ItemTradeHistoryResponseDto.class));

    when(itemTradeHistoryRepository.findAllByOrderByCreatedAtDesc()).thenReturn(entityList);
    when(itemTradeHistoryMapper.toDto(any())).thenReturn(dtoList.get(0));

    // when
    List<ItemTradeHistoryResponseDto> result = itemTradeHistoryService.getAllHistories();

    // then
    assertThat(result).hasSize(1);
    verify(itemTradeHistoryRepository).findAllByOrderByCreatedAtDesc();
  }

  @Test
  void getMyHistories_buyer() {
    // given
    String username = "buyer1";

    User buyer = User.builder()
        .username(username)
        .role(BUYER)
        .build();

    List<ItemTradeHistory> entityList = List.of(mock(ItemTradeHistory.class));
    List<ItemTradeHistoryResponseDto> dtoList = List.of(mock(ItemTradeHistoryResponseDto.class));

    when(userRepository.findById(username)).thenReturn(Optional.of(buyer));
    when(itemTradeHistoryRepository.findAllByBuyer_UsernameOrderByCreatedAtDesc(username)).thenReturn(entityList);
    when(itemTradeHistoryMapper.toDto(any())).thenReturn(dtoList.get(0));

    // when
    List<ItemTradeHistoryResponseDto> result = itemTradeHistoryService.getMyHistories(
        username);

    // then
    assertThat(result).hasSize(1);
    verify(itemTradeHistoryRepository).findAllByBuyer_UsernameOrderByCreatedAtDesc(username);
  }

  @Test
  void getMyHistories_seller() {
    // given
    String username = "seller1";

    User seller = User.builder()
        .username(username)
        .role(SELLER)
        .build();

    List<ItemTradeHistory> entityList = List.of(mock(ItemTradeHistory.class));
    List<ItemTradeHistoryResponseDto> dtoList = List.of(mock(ItemTradeHistoryResponseDto.class));

    when(userRepository.findById(username)).thenReturn(Optional.of(seller));
    when(itemTradeHistoryRepository.findAllByItemListing_Seller_UsernameOrderByCreatedAtDesc(username)).thenReturn(
        entityList);
    when(itemTradeHistoryMapper.toDto(any())).thenReturn(dtoList.get(0));

    // when
    List<ItemTradeHistoryResponseDto> result = itemTradeHistoryService.getMyHistories(
        username);

    // then
    assertThat(result).hasSize(1);
    verify(itemTradeHistoryRepository).findAllByItemListing_Seller_UsernameOrderByCreatedAtDesc(username);
  }

  @Test
  void getMyHistories_userNotFound() {
    // given
    String username = "ghost";

    when(userRepository.findById(username)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemTradeHistoryService.getMyHistories(username))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());
  }
}