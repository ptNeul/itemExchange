package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, String> {

}
