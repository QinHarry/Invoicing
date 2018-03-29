package com.halen.service.impl;

import com.halen.entity.Goods;
import com.halen.entity.PurchaseList;
import com.halen.entity.PurchaseListGoods;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.repository.PurchaseListGoodsRepository;
import com.halen.repository.PurchaseListRepository;
import com.halen.service.PurchaseListService;
import com.halen.util.MathUtil;
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

@Service("purchaseListService")
public class PurchaseListServiceImpl implements PurchaseListService {

    @Resource
    private PurchaseListRepository purchaseListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private PurchaseListGoodsRepository purchaseListGoodsRepository;

    @Override
    public String getTodayMaxPurchaseNumber() {
        return purchaseListRepository.getTodayMaxPurchaseNumber();
    }

    @Transactional
    public void save(PurchaseList purchaseList, List<PurchaseListGoods> purchaseListGoodsList) {
        for (PurchaseListGoods purchaseListGoods : purchaseListGoodsList) {
            purchaseListGoods.setType(goodsTypeRepository.findById(purchaseListGoods.getTypeId()).get());
            purchaseListGoods.setPurchaseList(purchaseList);
            purchaseListGoodsRepository.save(purchaseListGoods);

            Goods goods = goodsRepository.findById(purchaseListGoods.getGoodsId()).get();

            float svePurchasePrice = (goods.getPurchasingPrice() * goods.getInventoryQuantity() + purchaseListGoods.getPrice() * purchaseListGoods.getNum()) / (goods.getInventoryQuantity() + purchaseListGoods.getNum());
            goods.setPurchasingPrice(MathUtil.format2Bit(svePurchasePrice));
            goods.setInventoryQuantity(goods.getInventoryQuantity() + purchaseListGoods.getNum());
            goods.setLastPurchasingPrice(purchaseListGoods.getPrice());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        purchaseListRepository.save(purchaseList);
    }

    @Override
    public List<PurchaseList> list(PurchaseList purchaseList, Sort.Direction direction, String... properties) {
        return purchaseListRepository.findAll(new Specification<PurchaseList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<PurchaseList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (purchaseList != null) {
                    if (!StringUtil.isEmpty(purchaseList.getPurchaseNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("purchaseNumber"), "%" + purchaseList.getPurchaseNumber().trim() + "%"));
                    }
                    if (purchaseList.getSupplier() != null && purchaseList.getSupplier().getId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("supplier").get("id"), purchaseList.getSupplier().getId()));
                    }
                    if (purchaseList.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), purchaseList.getState()));
                    }
                    if (purchaseList.getBPurchaseDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("purchaseDate"), purchaseList.getBPurchaseDate()));
                    }
                    if (purchaseList.getEPurchaseDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("purchaseDate"), purchaseList.getEPurchaseDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public PurchaseList findById(Integer id) {
        if (purchaseListRepository.findById(id).isPresent()) {
            return purchaseListRepository.findById(id).get();
        }else {
            return null;
        }
    }

    @Transactional
    public void delete(Integer id) {
        purchaseListGoodsRepository.deleteByPurchaseListId(id);
        purchaseListRepository.deleteById(id);
    }

    @Override
    public void update(PurchaseList purchaseList) {
        purchaseListRepository.save(purchaseList);
    }
}
