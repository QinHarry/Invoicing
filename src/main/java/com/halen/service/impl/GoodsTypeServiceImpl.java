package com.halen.service.impl;

import com.halen.entity.GoodsType;
import com.halen.repository.GoodsTypeRepository;
import com.halen.service.GoodsTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("goodsTypeService")
public class GoodsTypeServiceImpl implements GoodsTypeService {

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Override
    public List<GoodsType> findByParentId(Integer parentId) {
        return goodsTypeRepository.findByParentId(parentId);
    }

    @Override
    public void save(GoodsType goodsType) {
        goodsTypeRepository.save(goodsType);
    }

    @Override
    public void delete(Integer id) {
        goodsTypeRepository.deleteById(id);
    }

    @Override
    public GoodsType findById(Integer id) {
        return goodsTypeRepository.findById(id).get();
    }
}
