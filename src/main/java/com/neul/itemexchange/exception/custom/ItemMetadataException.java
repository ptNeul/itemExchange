package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.BaseException;

public class ItemMetadataException extends BaseException {

  public ItemMetadataException(ItemMetadataErrorCode errorCode) {
    super(errorCode);
  }
}
