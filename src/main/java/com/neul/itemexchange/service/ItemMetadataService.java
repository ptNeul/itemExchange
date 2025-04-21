package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.*;

import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataPatchRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.exception.custom.ItemMetadataErrorCode;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.mapper.ItemMetadataMapper;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import java.util.List;
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

    if (itemMetadataRepository.existsByImageUrl(dto.getImageUrl())) {
      throw new ItemMetadataException(ItemMetadataErrorCode.DUPLICATE_IMAGE);
    }

    ItemMetadata entity = itemMetadataMapper.toEntity(dto);
    ItemMetadata saved = itemMetadataRepository.save(entity);

    return itemMetadataMapper.toDto(saved);
  }

  @Transactional(readOnly = true)
  public List<ItemMetadataResponseDto> readAll() {
    return itemMetadataRepository.findAll().stream()
        .map(itemMetadataMapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public ItemMetadataResponseDto readOne(Long itemId) {
    ItemMetadata entity = itemMetadataRepository.findById(itemId)
        .orElseThrow(() -> new ItemMetadataException(ITEM_NOT_FOUND));

    return itemMetadataMapper.toDto(entity);
  }

  @Transactional
  public ItemMetadataResponseDto patch(Long itemId, ItemMetadataPatchRequestDto dto) {
    ItemMetadata item = itemMetadataRepository.findById(itemId)
        .orElseThrow(() -> new ItemMetadataException(ITEM_NOT_FOUND));

    if (dto.getItemName() != null) {
      if (itemMetadataRepository.existsByItemName(dto.getItemName()) &&
          !dto.getItemName().equals(item.getItemName())) {
        throw new ItemMetadataException(DUPLICATE_ITEM_NAME);
      }
      item.setItemName(dto.getItemName());
    }

    if (dto.getImageUrl() != null) {
      if (itemMetadataRepository.existsByImageUrl(dto.getImageUrl()) &&
          !dto.getItemName().equals(item.getImageUrl())) {
        throw new ItemMetadataException(DUPLICATE_IMAGE);
      }
      item.setImageUrl(dto.getImageUrl());
    }

    if (dto.getDetail() != null) {
      item.setDetail(dto.getDetail());
    }

    return itemMetadataMapper.toDto(item);
  }

  @Transactional
  public void delete(Long itemId) {
    ItemMetadata item = itemMetadataRepository.findById(itemId)
        .orElseThrow(() -> new ItemMetadataException(ITEM_NOT_FOUND));

    itemMetadataRepository.delete(item);
  }
}
