package com.halen.repository;

import com.halen.entity.PurchaseListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseListGoodsRepository extends JpaRepository<PurchaseListGoods, Integer>, JpaSpecificationExecutor<PurchaseListGoods> {

    @Query(value = "SELECT * FROM t_purchase_list_goods WHERE purchase_list_id=?1", nativeQuery = true)
    public List<PurchaseListGoods> listByPurchaseListId(Integer purchaseListId);

    @Query(value = "DELETE FROM t_purchase_list_goods WHERE purchase_list_id=?1", nativeQuery = true)
    @Modifying
    public void deleteByPurchaseListId(Integer purchaseListId);
}
