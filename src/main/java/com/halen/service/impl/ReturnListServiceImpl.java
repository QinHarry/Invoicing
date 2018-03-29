package com.halen.service.impl;

import com.halen.entity.Goods;
import com.halen.entity.ReturnList;
import com.halen.entity.ReturnListGoods;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.repository.ReturnListGoodsRepository;
import com.halen.repository.ReturnListRepository;
import com.halen.service.ReturnListService;
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

@Service("returnListService")
public class ReturnListServiceImpl implements ReturnListService {

    @Resource
    private ReturnListRepository returnListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private ReturnListGoodsRepository returnListGoodsRepository;

    @Override
    public String getTodayMaxReturnNumber() {
        return returnListRepository.getTodayMaxReturnNumber();
    }

    @Transactional
    public void save(ReturnList returnList, List<ReturnListGoods> returnListGoodsList) {
        for(ReturnListGoods returnListGoods:returnListGoodsList){
            returnListGoods.setType(goodsTypeRepository.findById(returnListGoods.getTypeId()).get());
            returnListGoods.setReturnList(returnList);
            returnListGoodsRepository.save(returnListGoods);

            Goods goods=goodsRepository.findById(returnListGoods.getGoodsId()).get();

            goods.setInventoryQuantity(goods.getInventoryQuantity()-returnListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        returnListRepository.save(returnList); // 保存退货单
    }

    @Override
    public List<ReturnList> list(ReturnList returnList, Sort.Direction direction, String... properties) {
        return returnListRepository.findAll(new Specification<ReturnList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<ReturnList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (returnList != null) {
                    if (!StringUtil.isEmpty(returnList.getReturnNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("returnNumber"), "%" + returnList.getReturnNumber().trim() + "%"));
                    }
                    if (returnList.getSupplier() != null && returnList.getSupplier().getId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("supplier").get("id"), returnList.getSupplier().getId()));
                    }
                    if (returnList.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), returnList.getState()));
                    }
                    if (returnList.getBReturnDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("returnDate"), returnList.getBReturnDate()));
                    }
                    if (returnList.getEReturnDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("returnDate"), returnList.getEReturnDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public ReturnList findById(Integer id) {
        if (returnListRepository.findById(id).isPresent()) {
            return returnListRepository.findById(id).get();
        }else {
            return null;
        }
    }

    @Transactional
    public void delete(Integer id) {
        returnListGoodsRepository.deleteByReturnListId(id);
        returnListRepository.deleteById(id);
    }

    @Override
    public void update(ReturnList returnList) {
        returnListRepository.save(returnList);
    }
}
