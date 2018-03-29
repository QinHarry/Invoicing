package com.halen.service.impl;

import com.halen.entity.GoodsUnit;
import com.halen.repository.GoodsUnitRepository;
import com.halen.service.GoodsUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("goodsUnitService")
public class GoodsUnitServiceImpl implements GoodsUnitService {

    @Resource
    private GoodsUnitRepository goodsUnitRepository;

    @Override
    public List<GoodsUnit> listAll() {
        return goodsUnitRepository.findAll();
    }

    @Override
    public void save(GoodsUnit goodsUnit) {
        goodsUnitRepository.save(goodsUnit);
    }

    @Override
    public void delete(Integer id) {
        goodsUnitRepository.deleteById(id);
    }

    @Override
    public GoodsUnit findById(Integer id) {
        return goodsUnitRepository.findById(id).get();
    }
}
