package com.halen.service.impl;

import com.halen.entity.DamageList;
import com.halen.entity.DamageListGoods;
import com.halen.entity.Goods;
import com.halen.repository.DamageListGoodsRepository;
import com.halen.repository.DamageListRepository;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.service.DamageListService;
import com.halen.util.StringUtil;
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

@Service("damageListService")
public class DamageListServiceImpl implements DamageListService {

    @Resource
    private DamageListRepository damageListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private DamageListGoodsRepository damageListGoodsRepository;

    @Override
    public String getTodayMaxDamageNumber() {
        return damageListRepository.getTodayMaxDamageNumber();
    }

    @Transactional
    public void save(DamageList damageList, List<DamageListGoods> damageListGoodsList) {
        for(DamageListGoods damageListGoods:damageListGoodsList){
            damageListGoods.setType(goodsTypeRepository.findById(damageListGoods.getTypeId()).get());
            damageListGoods.setDamageList(damageList);
            damageListGoodsRepository.save(damageListGoods);
            Goods goods=goodsRepository.findById(damageListGoods.getGoodsId()).get();
            goods.setInventoryQuantity(goods.getInventoryQuantity()-damageListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        damageListRepository.save(damageList);
    }

    @Override
    public List<DamageList> list(DamageList damageList, Sort.Direction direction, String... properties) {
        return damageListRepository.findAll(new Specification<DamageList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<DamageList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (damageList != null) {
                    if(damageList!=null){
                        if(damageList.getBDamageDate()!=null){
                            predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("damageDate"), damageList.getBDamageDate()));
                        }
                        if(damageList.getEDamageDate()!=null){
                            predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("damageDate"), damageList.getEDamageDate()));
                        }
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public DamageList findById(Integer id) {
        if (damageListRepository.findById(id).isPresent()) {
            return damageListRepository.findById(id).get();
        }else {
            return null;
        }
    }

}
