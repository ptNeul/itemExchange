package com.neul.itemexchange.domain;

import jakarta.persistence.*;
import lombok.*;

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
