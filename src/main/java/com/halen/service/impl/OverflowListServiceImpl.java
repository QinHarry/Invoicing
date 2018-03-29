package com.halen.service.impl;

import com.halen.entity.OverflowList;
import com.halen.entity.OverflowListGoods;
import com.halen.entity.Goods;
import com.halen.repository.OverflowListGoodsRepository;
import com.halen.repository.OverflowListRepository;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.service.OverflowListService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("overflowListService")
public class OverflowListServiceImpl implements OverflowListService {

    @Resource
    private OverflowListRepository overflowListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private OverflowListGoodsRepository overflowListGoodsRepository;

    @Override
    public String getTodayMaxOverflowNumber() {
        return overflowListRepository.getTodayMaxOverflowNumber();
    }

    @Transactional
    public void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList) {
        for(OverflowListGoods overflowListGoods:overflowListGoodsList){
            overflowListGoods.setType(goodsTypeRepository.findById(overflowListGoods.getTypeId()).get());
            overflowListGoods.setOverflowList(overflowList);
            overflowListGoodsRepository.save(overflowListGoods);
            Goods goods = goodsRepository.findById(overflowListGoods.getGoodsId()).get();
            goods.setInventoryQuantity(goods.getInventoryQuantity()-overflowListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        overflowListRepository.save(overflowList);
    }

    @Override
    public List<OverflowList> list(OverflowList overflowList, Sort.Direction direction, String... properties) {
        return overflowListRepository.findAll(new Specification<OverflowList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<OverflowList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(overflowList!=null){
                    if(overflowList.getBOverflowDate()!=null){
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("overflowDate"), overflowList.getBOverflowDate()));
                    }
                    if(overflowList.getEOverflowDate()!=null){
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("overflowDate"), overflowList.getEOverflowDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public OverflowList findById(Integer id) {
        if (overflowListRepository.findById(id).isPresent()) {
            return overflowListRepository.findById(id).get();
        }else {
            return null;
        }
    }
}
