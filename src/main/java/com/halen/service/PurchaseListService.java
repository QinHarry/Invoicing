package com.halen.service;

import com.halen.entity.PurchaseList;
import com.halen.entity.PurchaseListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PurchaseListService {

    public PurchaseList findById(Integer id);

    public String getTodayMaxPurchaseNumber();

    public void save(PurchaseList purchaseList, List<PurchaseListGoods> purchaseListGoodsList);

    public List<PurchaseList> list(PurchaseList purchaseList, Sort.Direction direction, String...properties);

    public void delete(Integer id);

    public void update(PurchaseList purchaseList);
}
