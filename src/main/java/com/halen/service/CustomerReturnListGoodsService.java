package com.halen.service;

import com.halen.entity.CustomerReturnListGoods;

import java.util.List;

public interface CustomerReturnListGoodsService {

    public List<CustomerReturnListGoods> listByCustomerReturnListId(Integer customerReturnListId);

    public Integer getTotalByGoodsId(Integer goodsId);

    public List<CustomerReturnListGoods> list(CustomerReturnListGoods customerReturnListGoods);
}
