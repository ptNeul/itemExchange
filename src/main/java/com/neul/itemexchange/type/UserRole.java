package com.neul.itemexchange.type;

public enum UserRole {
  BUYER(0),
  SELLER(1),
  ADMIN(2);

  private final int value;

  UserRole(int value) {
    this.value = value;
  }

  public static UserRole fromValue(int value) {
    for (UserRole role : UserRole.values()) {
      if (role.value == value) {
        return role;
      }
    }
    throw new IllegalArgumentException("UserRole : 잘못된 value 값입니다.");
  }
}
