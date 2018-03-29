package com.halen.service.impl;

import com.halen.entity.Goods;
import com.halen.entity.SaleList;
import com.halen.entity.SaleListGoods;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.repository.SaleListGoodsRepository;
import com.halen.repository.SaleListRepository;
import com.halen.service.SaleListService;
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

@Service("saleListService")
public class SaleListServiceImpl implements SaleListService {

    @Resource
    private SaleListRepository saleListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private SaleListGoodsRepository saleListGoodsRepository;

    @Override
    public String getTodayMaxSaleNumber() {
        return saleListRepository.getTodayMaxSaleNumber();
    }

    @Transactional
    public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList) {
        for(SaleListGoods saleListGoods:saleListGoodsList){
            saleListGoods.setType(goodsTypeRepository.findById(saleListGoods.getTypeId()).get());
            saleListGoods.setSaleList(saleList);
            saleListGoodsRepository.save(saleListGoods);
            Goods goods=goodsRepository.findById(saleListGoods.getGoodsId()).get();
            goods.setInventoryQuantity(goods.getInventoryQuantity()-saleListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        saleListRepository.save(saleList);
    }

    @Override
    public List<SaleList> list(SaleList saleList, Sort.Direction direction, String... properties) {
        return saleListRepository.findAll(new Specification<SaleList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (saleList != null) {
                    if (!StringUtil.isEmpty(saleList.getSaleNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("saleNumber"), "%" + saleList.getSaleNumber().trim() + "%"));
                    }
                    if (saleList.getCustomer() != null && saleList.getCustomer().getId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), saleList.getCustomer().getId()));
                    }
                    if (saleList.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), saleList.getState()));
                    }
                    if (saleList.getBSaleDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("saleDate"), saleList.getBSaleDate()));
                    }
                    if (saleList.getESaleDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("saleDate"), saleList.getESaleDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public SaleList findById(Integer id) {
        if (saleListRepository.findById(id).isPresent()) {
            return saleListRepository.findById(id).get();
        }else {
            return null;
        }
    }

    @Transactional
    public void delete(Integer id) {
        saleListGoodsRepository.deleteBySaleListId(id);
        saleListRepository.deleteById(id);
    }

    @Override
    public void update(SaleList saleList) {
        saleListRepository.save(saleList);
    }

    @Override
    public List<Object> countSaleByDay(String begin, String end) {
        return saleListRepository.countSaleByDay(begin, end);
    }

    @Override
    public List<Object> countSaleByMonth(String begin, String end) {
        return saleListRepository.countSaleByMonth(begin, end);
    }
}
