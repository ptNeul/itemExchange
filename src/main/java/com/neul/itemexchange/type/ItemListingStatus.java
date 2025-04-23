package com.neul.itemexchange.type;

import java.util.HashMap;
import java.util.Map;

public enum ItemListingStatus {
  SALE(0),
  COMPLETED(1);

  private final int value;
  private static final Map<Integer, ItemListingStatus> map = new HashMap<>();

  static {
    for (ItemListingStatus status : ItemListingStatus.values()) {
      map.put(status.value, status);
    }
  }

  ItemListingStatus(int value) {
    this.value = value;
  }

  public static ItemListingStatus fromValue(int value) {
    ItemListingStatus status = map.get(value);
    if (status == null) {
      throw new IllegalArgumentException("ItemListingStatus : 잘못된 value 값입니다.");
    }
    return status;
  }

}
