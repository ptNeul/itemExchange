package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.*;

import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.exception.custom.ItemMetadataErrorCode;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.mapper.ItemMetadataMapper;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemMetadataService {

  private final ItemMetadataRepository itemMetadataRepository;
  private final ItemMetadataMapper itemMetadataMapper;

  @Transactional
  public ItemMetadataResponseDto create(ItemMetadataRequestDto dto) {
    if (itemMetadataRepository.existsByItemName(dto.getItemName())) {
      throw new ItemMetadataException(DUPLICATE_ITEM_NAME);
    }

    if (itemMetadataRepository.existsByImage(dto.getImage())) {
      throw new ItemMetadataException(ItemMetadataErrorCode.DUPLICATE_IMAGE);
    }

    ItemMetadata entity = itemMetadataMapper.toEntity(dto);
    ItemMetadata saved = itemMetadataRepository.save(entity);

    return itemMetadataMapper.toDto(saved);
  }
}
