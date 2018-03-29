package com.halen.service;

import com.halen.entity.GoodsType;

import java.util.List;

public interface GoodsTypeService {

    public List<GoodsType> findByParentId(Integer parentId);

    public void save(GoodsType goodsType);

    public void delete(Integer id);

    public GoodsType findById(Integer id);
}
