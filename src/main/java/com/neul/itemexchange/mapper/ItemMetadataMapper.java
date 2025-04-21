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

  public ItemMetadataResponseDto toDto(ItemMetadata item) {
    return ItemMetadataResponseDto.builder()
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .imageUrl(item.getImageUrl())
        .detail(item.getDetail())
        .build();
  }
}
