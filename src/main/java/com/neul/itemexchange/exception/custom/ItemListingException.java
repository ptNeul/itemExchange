package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.BaseException;

public class ItemListingException extends BaseException {

  public ItemListingException(ItemListingErrorCode errorCode) {
    super(errorCode);
  }
}
