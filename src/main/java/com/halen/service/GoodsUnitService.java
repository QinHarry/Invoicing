package com.halen.service;

import com.halen.entity.GoodsUnit;

import java.util.List;

public interface GoodsUnitService {


    public List<GoodsUnit> listAll();

    public void save(GoodsUnit goodsUnit);

    public void delete(Integer id);

    public GoodsUnit findById(Integer id);
}
