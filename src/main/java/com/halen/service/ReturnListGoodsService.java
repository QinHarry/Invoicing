package com.halen.service;

import com.halen.entity.ReturnListGoods;

import java.util.List;

public interface ReturnListGoodsService {

    public List<ReturnListGoods> listByReturnListId(Integer returnListId);

    public List<ReturnListGoods> list(ReturnListGoods returnListGoods);
}
