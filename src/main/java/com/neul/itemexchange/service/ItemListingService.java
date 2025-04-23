package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.ITEM_LISTING_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.ItemListingErrorCode.UNAUTHORIZED_ACCESS;
import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_METADATA_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemListingService {

  private final UserRepository userRepository;
  private final ItemMetadataRepository itemMetadataRepository;
  private final ItemListingRepository itemListingRepository;
  private final ItemListingMapper itemListingMapper;

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

  @Transactional
  public ItemListingResponseDto patch(Long listingId, ItemListingPatchRequestDto dto, String username) {
    ItemListing listing = itemListingRepository.findById(listingId)
        .orElseThrow(() -> new ItemListingException(ITEM_LISTING_NOT_FOUND));

    String sellerUsername = listing.getSeller().getUsername();
    if (!sellerUsername.equals(username)) {
      throw new ItemListingException(UNAUTHORIZED_ACCESS);
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
}
