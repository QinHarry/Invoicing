package com.halen.repository;

import com.halen.entity.PurchaseList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseListRepository extends JpaRepository<PurchaseList, Integer>, JpaSpecificationExecutor<PurchaseList> {

    @Query(value = "SELECT MAX(purchase_number) FROM t_purchase_list WHERE TO_DAYS(purchase_date) = TO_DAYS(NOW())", nativeQuery = true)
    public String getTodayMaxPurchaseNumber();
}
