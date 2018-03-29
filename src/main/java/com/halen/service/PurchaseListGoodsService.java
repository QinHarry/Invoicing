package com.halen.service;

import com.halen.entity.PurchaseListGoods;

import java.util.List;

public interface PurchaseListGoodsService {

    public List<PurchaseListGoods> listByPurchaseListId(Integer purchaseListId);

    public List<PurchaseListGoods> list(PurchaseListGoods purchaseListGoods);
}
