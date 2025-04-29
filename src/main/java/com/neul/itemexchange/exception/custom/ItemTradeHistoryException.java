package com.neul.itemexchange.exception.custom;

import com.neul.itemexchange.exception.BaseException;

public class ItemTradeHistoryException extends BaseException {

  public ItemTradeHistoryException(ItemTradeHistoryErrorCode errorCode) {
    super(errorCode);
  }
}
