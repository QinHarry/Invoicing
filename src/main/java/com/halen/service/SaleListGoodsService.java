package com.halen.service;

import com.halen.entity.SaleListGoods;

import java.util.List;

public interface SaleListGoodsService {

    public List<SaleListGoods> listBySaleListId(Integer saleListId);

    public Integer getTotalByGoodsId(Integer goodsId);

    public List<SaleListGoods> list(SaleListGoods saleListGoods);
}
