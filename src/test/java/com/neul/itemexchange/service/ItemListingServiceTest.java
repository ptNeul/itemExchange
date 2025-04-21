package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.ItemListingStatus.SALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neul.itemexchange.domain.ItemListing;
import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.ItemListingMapper;
import com.neul.itemexchange.repository.ItemListingRepository;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import com.neul.itemexchange.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemListingServiceTest {

  @InjectMocks
  ItemListingService itemListingService;

  @Mock
  UserRepository userRepository;

  @Mock
  ItemMetadataRepository itemMetadataRepository;

  @Mock
  ItemListingRepository itemListingRepository;

  @Mock
  ItemListingMapper itemListingMapper;

  @Test
  void create() {
    // given
    String username = "seller1";
    Long itemId = 1L;

    ItemListingRegisterRequestDto dto = ItemListingRegisterRequestDto.builder()
        .itemId(itemId)
        .price(2000)
        .quantity(5)
        .build();

    User user = User.builder().username(username).build();
    ItemMetadata metadata = ItemMetadata.builder().itemId(itemId).itemName("아이템").imageUrl("url")
        .build();
    ItemListing listing = ItemListing.builder().price(2000).quantity(5).status(SALE).build();
    ItemListing saved = ItemListing.builder().listingId(5L).price(2000).quantity(5).status(SALE)
        .seller(user).itemMetadata(metadata).build();

    ItemListingResponseDto expected = ItemListingResponseDto.builder()
        .listingId(5L)
        .sellerUsername(username)
        .itemId(itemId)
        .itemName("아이템")
        .itemImageUrl("url")
        .price(2000)
        .quantity(5)
        .status(SALE)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));
    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.of(metadata));
    when(itemListingMapper.toEntity(dto)).thenReturn(listing);
    when(itemListingRepository.save(listing)).thenReturn(saved);
    when(itemListingMapper.toDto(saved)).thenReturn(expected);

    // when
    ItemListingResponseDto result = itemListingService.create(username, dto);

    // then
    assertThat(result).isEqualTo(expected);
    verify(userRepository).findById(username);
    verify(itemMetadataRepository).findById(itemId);
    verify(itemListingRepository).save(listing);
    verify(itemListingMapper).toDto(saved);
  }

  @Test
  void create_userNotFound() {
    // given
    String username = "ghost";
    ItemListingRegisterRequestDto dto = ItemListingRegisterRequestDto.builder()
        .itemId(1L)
        .price(1000)
        .quantity(1)
        .build();

    when(userRepository.findById(username)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemListingService.create(username, dto))
        .isInstanceOf(UserException.class)
        .hasMessage(USER_NOT_FOUND.getMessage());

    verify(userRepository).findById(username);
    verify(itemMetadataRepository, never()).findById(any());
    verify(itemListingRepository, never()).save(any());
  }

  @Test
  void create_itemNotFound() {
    // given
    String username = "seller1";
    Long itemId = 999L;

    ItemListingRegisterRequestDto dto = ItemListingRegisterRequestDto.builder()
        .itemId(itemId)
        .price(1000)
        .quantity(1)
        .build();

    User user = User.builder().username(username).build();

    when(userRepository.findById(username)).thenReturn(Optional.of(user));
    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemListingService.create(username, dto))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(ITEM_NOT_FOUND.getMessage());

    verify(userRepository).findById(username);
    verify(itemMetadataRepository).findById(itemId);
    verify(itemListingRepository, never()).save(any());
  }
}