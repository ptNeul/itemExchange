package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.ItemListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListingRepository extends JpaRepository<ItemListing, Long> {

  List<ItemListing> findBySeller_UsernameOrderByStatusAscPriceAsc(String sellerUsername);

  List<ItemListing> findAllByOrderByStatusAscPriceAsc();

  List<ItemListing> findByItemMetadata_ItemNameContainingAndPriceBetweenOrderByStatusAscPriceAsc(
      String itemName, Integer min, Integer max);

  List<ItemListing> findByItemMetadata_ItemNameContainingOrderByStatusAscPriceAsc(String itemName);

  List<ItemListing> findByPriceBetweenOrderByStatusAscPriceAsc(Integer min, Integer max);
}
