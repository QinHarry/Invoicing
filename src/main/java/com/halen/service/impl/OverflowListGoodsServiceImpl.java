package com.halen.service.impl;

import com.halen.entity.OverflowListGoods;
import com.halen.repository.OverflowListGoodsRepository;
import com.halen.service.OverflowListGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("overflowListGoodsService")
public class OverflowListGoodsServiceImpl implements OverflowListGoodsService {

    @Resource
    private OverflowListGoodsRepository overflowListGoodsRepository;

    @Override
    public List<OverflowListGoods> listByOverflowListId(Integer overflowListId) {
        return overflowListGoodsRepository.listByOverflowListId(overflowListId);
    }
}
