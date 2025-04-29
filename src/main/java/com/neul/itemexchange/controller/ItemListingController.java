package com.neul.itemexchange.controller;

import com.neul.itemexchange.dto.itemListing.ItemListingPatchRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingRegisterRequestDto;
import com.neul.itemexchange.dto.itemListing.ItemListingResponseDto;
import com.neul.itemexchange.dto.itemListing.ItemPurchaseRequestDto;
import com.neul.itemexchange.service.ItemListingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/me")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<List<ItemListingResponseDto>> getMyListings(
      @AuthenticationPrincipal UserDetails userDetails) {
    List<ItemListingResponseDto> result = itemListingService.getMyListings(
        userDetails.getUsername());
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<List<ItemListingResponseDto>> getAllListings() {
    List<ItemListingResponseDto> result = itemListingService.getAllListings();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{listingId}")
  public ResponseEntity<ItemListingResponseDto> getOneListing(@PathVariable Long listingId) {
    ItemListingResponseDto result = itemListingService.getOneListing(listingId);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ItemListingResponseDto>> getFilteredListings(
      @RequestParam(required = false) String itemName,
      @RequestParam(required = false) Integer min,
      @RequestParam(required = false) Integer max) {
    List<ItemListingResponseDto> result = itemListingService.getFilteredListings(
        itemName, min, max);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{listingId}")
  public ResponseEntity<ItemListingResponseDto> patch(
      @PathVariable Long listingId,
      @RequestBody ItemListingPatchRequestDto dto,
      @AuthenticationPrincipal UserDetails userDetails) {
    ItemListingResponseDto result = itemListingService.patch(listingId, dto,
        userDetails.getUsername());
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{listingId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> delete(
      @PathVariable Long listingId,
      @AuthenticationPrincipal UserDetails userDetails) {
    itemListingService.delete(listingId, userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{listingId}/purchase")
  @PreAuthorize("hasRole('BUYER')")
  public ResponseEntity<Void> purchase(
      @PathVariable Long listingId,
      @RequestBody ItemPurchaseRequestDto dto,
      @AuthenticationPrincipal UserDetails userDetails) {
    itemListingService.purchase(listingId, userDetails.getUsername(), dto.getQuantity());
    return ResponseEntity.ok().build();
  }
}
