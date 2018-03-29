package com.halen.service.impl;

import com.halen.entity.CustomerReturnList;
import com.halen.entity.CustomerReturnListGoods;
import com.halen.entity.Goods;
import com.halen.repository.CustomerReturnListGoodsRepository;
import com.halen.repository.CustomerReturnListRepository;
import com.halen.repository.GoodsRepository;
import com.halen.repository.GoodsTypeRepository;
import com.halen.service.CustomerReturnListService;
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

@Service("customerReturnListService")
public class CustomerReturnListServiceImpl implements CustomerReturnListService {

    @Resource
    private CustomerReturnListRepository customerReturnListRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private CustomerReturnListGoodsRepository customerReturnListGoodsRepository;

    @Override
    public String getTodayMaxCustomerReturnNumber() {
        return customerReturnListRepository.getTodayMaxCustomerReturnNumber();
    }

    @Transactional
    public void save(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> customerReturnListGoodsList) {
        for(CustomerReturnListGoods customerReturnListGoods:customerReturnListGoodsList){
            customerReturnListGoods.setType(goodsTypeRepository.findById(customerReturnListGoods.getTypeId()).get());
            customerReturnListGoods.setCustomerReturnList(customerReturnList);
            customerReturnListGoodsRepository.save(customerReturnListGoods);
            Goods goods=goodsRepository.findById(customerReturnListGoods.getGoodsId()).get();
            goods.setInventoryQuantity(goods.getInventoryQuantity() + customerReturnListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        customerReturnListRepository.save(customerReturnList);
    }

    @Override
    public List<CustomerReturnList> list(CustomerReturnList customerReturnList, Sort.Direction direction, String... properties) {
        return customerReturnListRepository.findAll(new Specification<CustomerReturnList>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CustomerReturnList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (customerReturnList != null) {
                    if (!StringUtil.isEmpty(customerReturnList.getCustomerReturnNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("customerReturnNumber"), "%" + customerReturnList.getCustomerReturnNumber().trim() + "%"));
                    }
                    if (customerReturnList.getCustomer() != null && customerReturnList.getCustomer().getId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), customerReturnList.getCustomer().getId()));
                    }
                    if (customerReturnList.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), customerReturnList.getState()));
                    }
                    if (customerReturnList.getBCustomerReturnDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.getBCustomerReturnDate()));
                    }
                    if (customerReturnList.getECustomerReturnDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.getECustomerReturnDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public CustomerReturnList findById(Integer id) {
        if (customerReturnListRepository.findById(id).isPresent()) {
            return customerReturnListRepository.findById(id).get();
        }else {
            return null;
        }
    }

    @Transactional
    public void delete(Integer id) {
        customerReturnListGoodsRepository.deleteByCustomerReturnListId(id);
        customerReturnListRepository.deleteById(id);
    }

    @Override
    public void update(CustomerReturnList customerReturnList) {
        customerReturnListRepository.save(customerReturnList);
    }
}
