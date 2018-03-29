package com.halen.service;

import com.halen.entity.OverflowListGoods;

import java.util.List;

public interface OverflowListGoodsService {

    public List<OverflowListGoods> listByOverflowListId(Integer overflowListId);
}
