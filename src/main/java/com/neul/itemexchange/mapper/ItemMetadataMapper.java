package com.neul.itemexchange.mapper;

import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ItemMetadataMapper {

  public ItemMetadata toEntity(ItemMetadataRequestDto dto) {
    return ItemMetadata.builder()
        .itemName(dto.getItemName())
        .imageUrl(dto.getImageUrl())
        .detail(dto.getDetail())
        .build();
  }

  public ItemMetadataResponseDto toDto(ItemMetadata entity) {
    return ItemMetadataResponseDto.builder()
        .itemId(entity.getItemId())
        .itemName(entity.getItemName())
        .imageUrl(entity.getImageUrl())
        .detail(entity.getDetail())
        .build();
  }
}
