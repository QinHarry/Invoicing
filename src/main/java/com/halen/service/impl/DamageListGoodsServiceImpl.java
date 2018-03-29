package com.halen.service.impl;

import com.halen.entity.DamageListGoods;
import com.halen.repository.DamageListGoodsRepository;
import com.halen.service.DamageListGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("damageListGoodsService")
public class DamageListGoodsServiceImpl implements DamageListGoodsService {

    @Resource
    private DamageListGoodsRepository damageListGoodsRepository;

    @Override
    public List<DamageListGoods> listByDamageListId(Integer damageListId) {
        return damageListGoodsRepository.listByDamageListId(damageListId);
    }
}
