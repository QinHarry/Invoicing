package com.halen.service;

import com.halen.entity.DamageListGoods;

import java.util.List;

public interface DamageListGoodsService {

    public List<DamageListGoods> listByDamageListId(Integer damageListId);
}
