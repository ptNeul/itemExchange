package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.ItemListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListingRepository extends JpaRepository<ItemListing, Long> {

  List<ItemListing> findBySeller_Username(String sellerUsername);
}
