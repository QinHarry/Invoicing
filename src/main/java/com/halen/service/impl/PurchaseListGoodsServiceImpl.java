package com.halen.service.impl;

import com.halen.entity.PurchaseListGoods;
import com.halen.repository.PurchaseListGoodsRepository;
import com.halen.service.PurchaseListGoodsService;
import com.halen.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("purchaseListGoodsService")
public class PurchaseListGoodsServiceImpl implements PurchaseListGoodsService {

    @Resource
    private PurchaseListGoodsRepository purchaseListGoodsRepository;

    @Override
    public List<PurchaseListGoods> listByPurchaseListId(Integer purchaseListId) {
        return purchaseListGoodsRepository.listByPurchaseListId(purchaseListId);
    }

    @Override
    public List<PurchaseListGoods> list(PurchaseListGoods purchaseListGoods) {
        return purchaseListGoodsRepository.findAll(new Specification<PurchaseListGoods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<PurchaseListGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (purchaseListGoods != null) {
                    if (purchaseListGoods.getType() != null && purchaseListGoods.getType().getId() != null && purchaseListGoods.getType().getId() != 1) {
                        predicate.getExpressions().add(cb.equal(root.get("type").get("id"), purchaseListGoods.getType().getId()));
                    }
                    if (!StringUtil.isEmpty(purchaseListGoods.getCodeOrName())) {
                        predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+purchaseListGoods.getCodeOrName()+"%"), cb.like(root.get("name"), "%"+purchaseListGoods.getCodeOrName()+"%")));
                    }
                }
                return predicate;
            }
        });
    }
}
