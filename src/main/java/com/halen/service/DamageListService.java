package com.halen.service;

import com.halen.entity.DamageList;
import com.halen.entity.DamageListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DamageListService {

    public DamageList findById(Integer id);

    public String getTodayMaxDamageNumber();

    public void save(DamageList damageList, List<DamageListGoods> damageListGoodsList);

    public List<DamageList> list(DamageList damageList, Sort.Direction direction, String...properties);
}
