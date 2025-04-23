package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.CANNOT_MODIFY_COMPLETED_ITEM;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.INVALID_PRICE_RANGE;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.INVALID_PURCHASE_QUANTITY;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.ITEM_LISTING_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.NOT_ENOUGH_ITEM_QUANTITY;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.PRICE_RANGE_REQUIRED;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.UNAUTHORIZED_ACCESS;
import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_METADATA_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.UserErrorCode.INSUFFICIENT_BALANCE;
import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;
import static com.neul.itemexchange.type.ItemListingStatus.COMPLETED;

import com.neul.itemexchange.domain.ItemListing;
import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.domain.ItemTradeHistory;
import com.neul.itemexchange.domain.User;
import com.neul.itemexchange.dto.itemListing.ItemListingPatchRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.exception.custom.ItemListingErrorCode;
import com.neul.itemexchange.exception.custom.ItemListingException;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.exception.custom.UserException;
import com.neul.itemexchange.mapper.ItemListingMapper;
import com.neul.itemexchange.repository.ItemListingRepository;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import com.neul.itemexchange.repository.ItemTradeHistoryRepository;
import com.neul.itemexchange.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemListingService {

  private final UserService userService;
  private final UserRepository userRepository;
  private final ItemMetadataRepository itemMetadataRepository;
  private final ItemListingRepository itemListingRepository;
  private final ItemListingMapper itemListingMapper;
  private final ItemTradeHistoryRepository itemTradeHistoryRepository;

  /**
   * 판매정보 등록
   */
  @Transactional
  public ItemListingResponseDto create(String username, ItemListingRegisterRequestDto dto) {
    User seller = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    ItemMetadata itemMetadata = itemMetadataRepository.findById(dto.getItemId())
        .orElseThrow(() -> new ItemMetadataException(ITEM_METADATA_NOT_FOUND));

    ItemListing itemListing = itemListingMapper.toEntity(dto);
    itemListing.setSeller(seller);
    itemListing.setItemMetadata(itemMetadata);

    ItemListing saved = itemListingRepository.save(itemListing);
    return itemListingMapper.toDto(saved);
  }

  /**
   * 내(Seller) 판매목록 조회
   */
  @Transactional(readOnly = true)
  public List<ItemListingResponseDto> getMyListings(String username) {
    return itemListingRepository.findBySeller_UsernameOrderByStatusAscPriceAsc(username).stream()
        .map(itemListingMapper::toDto)
        .toList();
  }

  /**
   * 전체 판매목록 조회
   */
  @Transactional(readOnly = true)
  public List<ItemListingResponseDto> getAllListings() {
    return itemListingRepository.findAllByOrderByStatusAscPriceAsc().stream()
        .map(itemListingMapper::toDto)
        .toList();
  }

  /**
   * 단일 판매정보 조회
   */
  @Transactional(readOnly = true)
  public ItemListingResponseDto getOneListing(Long listingId) {
    ItemListing itemListing = itemListingRepository.findById(listingId)
        .orElseThrow(() -> new ItemListingException(ITEM_LISTING_NOT_FOUND));

    return itemListingMapper.toDto(itemListing);
  }

  /**
   * 필터링 판매정보 조회
   */
  @Transactional(readOnly = true)
  public List<ItemListingResponseDto> getFilteredListings(String itemName, Integer min,
      Integer max) {

    // min만 있거나 max만 있을 경우
    if ((min != null && max == null) || (min == null && max != null)) {
      throw new ItemListingException(PRICE_RANGE_REQUIRED);
    }

    // 둘 다 있을 경우 범위 검증
    if (min != null && min > max) {
      throw new ItemListingException(INVALID_PRICE_RANGE);
    }

    List<ItemListing> result;

    if (itemName != null && !itemName.isBlank()) {
      if (min != null) {
        result = itemListingRepository
            .findByItemMetadata_ItemNameContainingAndPriceBetweenOrderByStatusAscPriceAsc(
                itemName, min, max);
      } else {
        result = itemListingRepository.findByItemMetadata_ItemNameContainingOrderByStatusAscPriceAsc(
            itemName);
      }
    } else {
      if (min != null) {
        result = itemListingRepository.findByPriceBetweenOrderByStatusAscPriceAsc(min,
            max);
      } else {
        result = itemListingRepository.findAllByOrderByStatusAscPriceAsc();
      }
    }

    return result.stream()
        .map(itemListingMapper::toDto)
        .toList();
  }

  @Transactional
  public ItemListingResponseDto patch(Long listingId, ItemListingPatchRequestDto dto,
      String username) {
    ItemListing listing = itemListingRepository.findById(listingId)
        .orElseThrow(() -> new ItemListingException(ITEM_LISTING_NOT_FOUND));

    String sellerUsername = listing.getSeller().getUsername();
    if (!sellerUsername.equals(username)) {
      throw new ItemListingException(UNAUTHORIZED_ACCESS);
    }

    if (listing.getStatus() == COMPLETED) {
      throw new ItemListingException(CANNOT_MODIFY_COMPLETED_ITEM);
    }

    if (dto.getPrice() > 0) {
      listing.setPrice(dto.getPrice());
    }
    if (dto.getQuantity() > 0) {
      listing.setQuantity(dto.getQuantity());
    }

    return itemListingMapper.toDto(listing);
  }

  @Transactional
  public void delete(Long listingId, String username) {
    ItemListing listing = itemListingRepository.findById(listingId)
        .orElseThrow(() -> new ItemListingException(ITEM_LISTING_NOT_FOUND));

    String sellerUsername = listing.getSeller().getUsername();
    if (!sellerUsername.equals(username)) {
      throw new ItemListingException(UNAUTHORIZED_ACCESS);
    }

    itemListingRepository.delete(listing);
  }

  @Transactional
  public void purchase(Long listingId, String buyerUsername, int quantity) {
    if (quantity <= 0) {
      throw new ItemListingException(INVALID_PURCHASE_QUANTITY);
    }

    ItemListing listing = itemListingRepository.findById(listingId)
        .orElseThrow(() -> new ItemListingException(ITEM_LISTING_NOT_FOUND));

    User buyer = userRepository.findById(buyerUsername)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    User seller = listing.getSeller();

    if (listing.getQuantity() < quantity) {
      throw new ItemListingException(NOT_ENOUGH_ITEM_QUANTITY);
    }

    long totalPrice = (long) listing.getPrice() * quantity;

    if (buyer.getBalance() < totalPrice) {
      throw new UserException(INSUFFICIENT_BALANCE);
    }

    userService.transferBalance(buyer.getUsername(), seller.getUsername(), totalPrice);

    listing.decreaseQuantity(quantity);

    if (listing.getQuantity() == 0) {
      listing.markAsCompleted();
    }

    ItemTradeHistory itemTradeHistory = ItemTradeHistory.builder()
        .itemListing(listing)
        .buyer(buyer)
        .quantity(quantity)
        .totalPrice(totalPrice)
        .build();

    itemTradeHistoryRepository.save(itemTradeHistory);
  }
}
