package com.neul.itemexchange.type;

import java.util.HashMap;
import java.util.Map;

public enum UserRole {
  BUYER(0),
  SELLER(1),
  ADMIN(2);

  private final int value;
  private static final Map<Integer, UserRole> map = new HashMap<>();

  static {
    for (UserRole role : UserRole.values()) {
      map.put(role.value, role);
    }
  }

  UserRole(int value) {
    this.value = value;
  }

  public static UserRole fromValue(int value) {
    UserRole role = map.get(value);
    if (role == null) {
      throw new IllegalArgumentException("UserRole : 잘못된 value 값입니다.");
    }
    return role;
  }
}
