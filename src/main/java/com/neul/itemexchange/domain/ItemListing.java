package com.neul.itemexchange.domain;

import static com.neul.itemexchange.type.ItemListingStatus.COMPLETED;

import com.neul.itemexchange.type.ItemListingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_listing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemListing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long listingId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", referencedColumnName = "username", nullable = false)
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private ItemMetadata itemMetadata;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private int quantity;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private ItemListingStatus status;

  public void decreaseQuantity(int amount) {
    this.quantity -= amount;
  }

  public void markAsCompleted() {
    this.status = COMPLETED;
  }
}
