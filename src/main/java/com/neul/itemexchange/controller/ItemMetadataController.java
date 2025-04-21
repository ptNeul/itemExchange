package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.itemmetadata.ItemMetadataPatchRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.service.ItemMetadataService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping
  public ResponseEntity<List<ItemMetadataResponseDto>> readAll() {
    List<ItemMetadataResponseDto> result = itemMetadataService.readAll();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{itemId}")
  public ResponseEntity<ItemMetadataResponseDto> readOne(@PathVariable Long itemId) {
    ItemMetadataResponseDto result = itemMetadataService.readOne(itemId);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{itemId}")
  public ResponseEntity<ItemMetadataResponseDto> patch(
      @PathVariable Long itemId,
      @RequestBody ItemMetadataPatchRequestDto dto) {
    ItemMetadataResponseDto result = itemMetadataService.patch(itemId, dto);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{itemId}")
  public ResponseEntity<Void> delete(@PathVariable Long itemId) {
    itemMetadataService.delete(itemId);
    return ResponseEntity.noContent().build();
  }
}
