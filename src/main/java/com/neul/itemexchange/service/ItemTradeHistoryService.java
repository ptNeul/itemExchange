package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.UserRole.BUYER;
import static com.neul.itemexchange.type.UserRole.SELLER;

import com.neul.itemexchange.domain.ItemTradeHistory;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.itemtradehistory.ItemTradeHistoryResponseDto;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.ItemTradeHistoryMapper;
import com.neul.itemexchange.repository.ItemTradeHistoryRepository;
import com.neul.itemexchange.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemTradeHistoryService {

  private final ItemTradeHistoryRepository itemTradeHistoryRepository;
  private final UserRepository userRepository;
  private final ItemTradeHistoryMapper itemTradeHistoryMapper;

  public List<ItemTradeHistoryResponseDto> getAllHistories() {
    return itemTradeHistoryRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(itemTradeHistoryMapper::toDto)
        .toList();
  }

  public List<ItemTradeHistoryResponseDto> getMyHistories(String username) {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    List<ItemTradeHistory> result;

    if (user.getRole().equals(BUYER)) {
      result = itemTradeHistoryRepository.findAllByBuyer_UsernameOrderByCreatedAtDesc(username);
    } else if (user.getRole().equals(SELLER)) {
      result = itemTradeHistoryRepository.findAllByItemListing_Seller_UsernameOrderByCreatedAtDesc(username);
    } else {
      result = List.of();
    }

    return result.stream()
        .map(itemTradeHistoryMapper::toDto)
        .toList();
  }
}
