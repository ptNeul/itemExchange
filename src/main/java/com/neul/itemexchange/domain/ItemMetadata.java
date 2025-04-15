package com.neul.itemexchange.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMetadata {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long itemId;

  @Column(name = "item_name", length = 50, nullable = false, unique = true)
  private String itemName;

  @Column(length = 255, nullable = false, unique = true)
  private String image;

  @Column(length = 255, nullable = false)
  private String detail;
}