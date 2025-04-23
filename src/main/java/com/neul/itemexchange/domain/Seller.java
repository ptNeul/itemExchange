package com.neul.itemexchange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/**
 * Seller 회원가입 시 Seller 정보 추가 등록
 */

public class Seller {

  @Id
  @Column(name = "seller_id", length = 50)
  private String sellerId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "seller_id")
  private User user;

  @Column(name = "account_number", length = 20, nullable = false)
  private String accountNumber;

  @Column(name = "business_number", length = 20, nullable = false)
  private String businessNumber;
}
