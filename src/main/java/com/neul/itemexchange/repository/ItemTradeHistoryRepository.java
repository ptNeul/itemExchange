package com.neul.itemexchange.repository;

import com.neul.itemexchange.domain.ItemTradeHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTradeHistoryRepository extends JpaRepository<ItemTradeHistory, Long> {

  List<ItemTradeHistory> findAllByOrderByCreatedAtDesc();

  // 구매자가 본인 거래 내역 조회
  List<ItemTradeHistory> findAllByBuyer_UsernameOrderByCreatedAtDesc(String buyerUsername);

  // 판매자가 본인 아이템과 관련된 거래 내역 조회
  List<ItemTradeHistory> findAllByItemListing_Seller_UsernameOrderByCreatedAtDesc(String sellerUsername);
}
