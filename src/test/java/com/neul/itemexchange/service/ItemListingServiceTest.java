package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.ITEM_LISTING_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.UNAUTHORIZED_ACCESS;
import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_METADATA_NOT_FOUND;
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
import com.neul.itemexchange.dto.itemListing.ItemListingPatchRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.exception.custom.ItemListingException;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.ItemListingMapper;
import com.neul.itemexchange.repository.ItemListingRepository;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import com.neul.itemexchange.repository.UserRepository;
import java.util.List;
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
        .hasMessage(ITEM_METADATA_NOT_FOUND.getMessage());

    verify(userRepository).findById(username);
    verify(itemMetadataRepository).findById(itemId);
    verify(itemListingRepository, never()).save(any());
  }

  @Test
  void getMyListing() {
    // given
    String username = "seller1";
    ItemListing listing = ItemListing.builder().build();
    ItemListingResponseDto dto = ItemListingResponseDto.builder().listingId(1L).build();

    when(itemListingRepository.findBySeller_UsernameOrderByStatusAscPriceAsc(username)).thenReturn(
        List.of(listing));
    when(itemListingMapper.toDto(listing)).thenReturn(dto);

    // when
    List<ItemListingResponseDto> result = itemListingService.getMyListings(username);

    // then
    assertThat(result).hasSize(1).contains(dto);
    verify(itemListingRepository).findBySeller_UsernameOrderByStatusAscPriceAsc(username);
    verify(itemListingMapper).toDto(listing);
  }

  @Test
  void getAllListing() {
    // given
    ItemListing listing = ItemListing.builder().build();
    ItemListingResponseDto dto = ItemListingResponseDto.builder().listingId(1L).build();

    when(itemListingRepository.findAllByOrderByStatusAscPriceAsc()).thenReturn(List.of(listing));
    when(itemListingMapper.toDto(listing)).thenReturn(dto);

    // when
    List<ItemListingResponseDto> result = itemListingService.getAllListings();

    // then
    assertThat(result).hasSize(1).contains(dto);
    verify(itemListingRepository).findAllByOrderByStatusAscPriceAsc();
    verify(itemListingMapper).toDto(listing);
  }

  @Test
  void getOneListing() {
    // given
    Long id = 1L;
    ItemListing listing = ItemListing.builder().listingId(id).build();
    ItemListingResponseDto dto = ItemListingResponseDto.builder().listingId(id).build();

    when(itemListingRepository.findById(id)).thenReturn(Optional.of(listing));
    when(itemListingMapper.toDto(listing)).thenReturn(dto);

    // when
    ItemListingResponseDto result = itemListingService.getOneListing(id);

    // then
    assertThat(result.getListingId()).isEqualTo(id);
    verify(itemListingRepository).findById(id);
    verify(itemListingMapper).toDto(listing);
  }

  @Test
  void getOneListing_itemListingNotFound() {
    // given
    Long id = 999L;

    when(itemListingRepository.findById(id)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemListingService.getOneListing(id))
        .isInstanceOf(ItemListingException.class)
        .hasMessage(ITEM_LISTING_NOT_FOUND.getMessage());

    verify(itemListingRepository).findById(id);
    verify(itemListingMapper, never()).toDto(any());
  }

  @Test
  void patch() {
    // given
    Long listingId = 1L;
    String username = "seller1";

    ItemListingPatchRequestDto dto = ItemListingPatchRequestDto.builder()
        .price(3000)
        .quantity(10)
        .build();

    User seller = User.builder().username(username).build();
    ItemListing listing = ItemListing.builder()
        .listingId(listingId)
        .price(2000)
        .quantity(5)
        .seller(seller)
        .build();

    ItemListingResponseDto expected = ItemListingResponseDto.builder()
        .listingId(listingId)
        .sellerUsername(username)
        .price(3000)
        .quantity(10)
        .build();

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.of(listing));
    when(itemListingMapper.toDto(any())).thenReturn(expected);

    // when
    ItemListingResponseDto result = itemListingService.patch(listingId, dto, username);

    // then
    assertThat(result).isEqualTo(expected);
    assertThat(listing.getPrice()).isEqualTo(3000);
    assertThat(listing.getQuantity()).isEqualTo(10);
    verify(itemListingRepository).findById(listingId);
    verify(itemListingMapper).toDto(listing);
  }

  @Test
  void patch_itemListingNotFound() {
    // given
    Long listingId = 999L;
    String username = "seller1";

    ItemListingPatchRequestDto dto = ItemListingPatchRequestDto.builder()
        .price(1000)
        .quantity(1)
        .build();

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemListingService.patch(listingId, dto, username))
        .isInstanceOf(ItemListingException.class)
        .hasMessage(ITEM_LISTING_NOT_FOUND.getMessage());

    verify(itemListingRepository).findById(listingId);
    verify(itemListingMapper, never()).toDto(any());
  }

  @Test
  void patch_unauthorizedAccess() {
    // given
    Long listingId = 1L;
    String actualSeller = "seller1";
    String currentSeller = "otherSeller";

    ItemListingPatchRequestDto dto = ItemListingPatchRequestDto.builder()
        .price(3000)
        .quantity(10)
        .build();

    User seller = User.builder().username(actualSeller).build();
    ItemListing listing = ItemListing.builder()
        .listingId(listingId)
        .price(2000)
        .quantity(5)
        .seller(seller)
        .build();

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.of(listing));

    // when
    // then
    assertThatThrownBy(() -> itemListingService.patch(listingId, dto, currentSeller))
        .isInstanceOf(ItemListingException.class)
        .hasMessage(UNAUTHORIZED_ACCESS.getMessage());

    verify(itemListingRepository).findById(listingId);
    verify(itemListingMapper, never()).toDto(any());
  }

  @Test
  void delete() {
    // given
    Long listingId = 1L;
    String username = "seller1";

    User seller = User.builder().username(username).build();
    ItemListing listing = ItemListing.builder()
        .listingId(listingId)
        .seller(seller)
        .build();

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.of(listing));

    // when
    itemListingService.delete(listingId, username);

    // then
    verify(itemListingRepository).findById(listingId);
    verify(itemListingRepository).delete(listing);
  }

  @Test
  void delete_itemListingNotFound() {
    // given
    Long listingId = 1L;
    String username = "seller1";

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemListingService.delete(listingId, username))
        .isInstanceOf(ItemListingException.class)
        .hasMessage(ITEM_LISTING_NOT_FOUND.getMessage());

    verify(itemListingRepository).findById(listingId);
    verify(itemListingRepository, never()).delete(any());
  }

  @Test
  void delete_unauthorizedAccess() {
    // given
    Long listingId = 1L;
    String actualSeller = "seller1";
    String currentSeller = "otherSeller";

    User seller = User.builder().username(actualSeller).build();
    ItemListing listing = ItemListing.builder()
        .listingId(listingId)
        .seller(seller)
        .build();

    when(itemListingRepository.findById(listingId)).thenReturn(Optional.of(listing));

    // when
    // then
    assertThatThrownBy(() -> itemListingService.delete(listingId, currentSeller))
        .isInstanceOf(ItemListingException.class)
        .hasMessage(UNAUTHORIZED_ACCESS.getMessage());

    verify(itemListingRepository).findById(listingId);
    verify(itemListingRepository, never()).delete(any());
  }
}