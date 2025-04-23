package com.neul.itemexchange.type;

public enum ItemListingStatus {
  SALE(0),
  COMPLETED(1);

  private final int value;

  ItemListingStatus(int value) {
    this.value = value;
  }

  public static ItemListingStatus fromValue(int value) {
    for (ItemListingStatus status : ItemListingStatus.values()) {
      if (status.value == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("ItemListingStatus : 잘못된 value 값입니다.");
  }

}
