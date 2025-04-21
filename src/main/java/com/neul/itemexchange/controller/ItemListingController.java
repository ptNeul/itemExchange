package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.service.ItemListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-listings")
public class ItemListingController {

  private final ItemListingService itemListingService;

  @PostMapping
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<ItemListingResponseDto> create(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody ItemListingRegisterRequestDto dto) {
    ItemListingResponseDto result = itemListingService.create(userDetails.getUsername(), dto);
    return ResponseEntity.ok(result);
  }
}
