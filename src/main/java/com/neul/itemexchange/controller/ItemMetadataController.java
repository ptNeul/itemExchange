package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.service.ItemMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins/item-metadatas")
public class ItemMetadataController {

  private final ItemMetadataService itemMetadataService;

  @PostMapping
  public ResponseEntity<ItemMetadataResponseDto> create(@RequestBody ItemMetadataRequestDto dto) {
    ItemMetadataResponseDto result = itemMetadataService.create(dto);
    return ResponseEntity.ok(result);
  }
}
