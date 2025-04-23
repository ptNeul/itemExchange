package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.ItemMetadata;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemMetadataRepository extends JpaRepository<ItemMetadata, Long> {

  Optional<ItemMetadata> findByItemName(String itemName);

  boolean existsByItemName(String itemName);

  boolean existsByImageUrl(String imageUrl);

  List<ItemMetadata> findAll();
}
