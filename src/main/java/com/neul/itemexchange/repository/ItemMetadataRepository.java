package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.ItemMetadata;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemMetadataRepository extends JpaRepository<ItemMetadata, Long> {

  Optional<ItemMetadata> findByItemName(String itemName);

  boolean existsByItemName(String itemName);

  boolean existsByImage(String image);

  List<ItemMetadata> findAll();
}
