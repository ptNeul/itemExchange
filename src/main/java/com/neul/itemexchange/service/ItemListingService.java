package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_NOT_FOUND;
import static com.neul.itemexchange.exception.custom.UserErrorCode.USER_NOT_FOUND;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemListingService {

  private final UserRepository userRepository;
  private final ItemMetadataRepository itemMetadataRepository;
  private final ItemListingRepository itemListingRepository;
  private final ItemListingMapper itemListingMapper;

  @Transactional
  public ItemListingResponseDto create(String username, ItemListingRegisterRequestDto dto) {
    User seller = userRepository.findById(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    ItemMetadata itemMetadata = itemMetadataRepository.findById(dto.getItemId())
        .orElseThrow(() -> new ItemMetadataException(ITEM_NOT_FOUND));

    ItemListing itemListing = itemListingMapper.toEntity(dto);
    itemListing.setSeller(seller);
    itemListing.setItemMetadata(itemMetadata);

    ItemListing saved = itemListingRepository.save(itemListing);
    return itemListingMapper.toDto(saved);
  }
}
