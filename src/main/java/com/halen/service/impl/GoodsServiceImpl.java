package com.halen.service.impl;

import com.halen.entity.Goods;
import com.halen.entity.User;
import com.halen.repository.GoodsRepository;
import com.halen.service.GoodsService;
import com.halen.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsRepository goodsRepository;

    @Override
    public List<Goods> findByTypeId(Integer typeId) {
        return goodsRepository.findByTypeId(typeId);
    }

    @Override
    public List<Goods> list(Goods goods, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Goods> pageGoods = goodsRepository.findAll(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (goods != null) {
                    if (!StringUtil.isEmpty(goods.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + goods.getName() + "%"));
                    }
                    if (goods.getType() != null && goods.getType().getId() != null && goods.getType().getId() != 1) {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("type").get("id"), goods.getType().getId()));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageGoods.getContent();
    }

    @Override
    public Long getCount(Goods goods) {
        Long count = goodsRepository.count(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (goods != null) {
                    if (!StringUtil.isEmpty(goods.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + goods.getName() + "%"));
                    }
                    if (goods.getType() != null && goods.getType().getId() != null && goods.getType().getId() != 1) {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("type").get("id"), goods.getType().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public List<Goods> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Goods> pageGoods = goodsRepository.findAll(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (codeOrName != null) {
                    predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + codeOrName + "%"), cb.like(root.get("code"), "%" + codeOrName + "%")));
                }
                predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0));
                return predicate;
            }
        }, pageable);
        return pageGoods.getContent();
    }

    @Override
    public Long getCountNoInventoryQuantityByCodeOrName(String codeOrName) {
        Long count = goodsRepository.count(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (codeOrName != null) {
                    predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + codeOrName + "%"), cb.like(root.get("code"), "%" + codeOrName + "%")));
                }
                predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0));
                return predicate;
            }
        });
        return count;
    }

    @Override
    public List<Goods> listHasInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Goods> pageGoods = goodsRepository.findAll(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (codeOrName != null) {
                    predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + codeOrName + "%"), cb.like(root.get("code"), "%" + codeOrName + "%")));
                }
                predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0));
                return predicate;
            }
        }, pageable);
        return pageGoods.getContent();
    }

    @Override
    public Long getCountHasInventoryQuantityByCodeOrName(String codeOrName) {
        Long count = goodsRepository.count(new Specification<Goods>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (codeOrName != null) {
                    predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + codeOrName + "%"), cb.like(root.get("code"), "%" + codeOrName + "%")));
                }
                predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0));
                return predicate;
            }
        });
        return count;
    }

    @Override
    public String getMaxGoodsCode() {
        return goodsRepository.getMaxGoodsCode();
    }

    @Override
    public void save(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public Goods findById(Integer id) {
        return goodsRepository.findById(id).get();
    }

    @Override
    public void delete(Integer id) {
        goodsRepository.deleteById(id);
    }

    @Override
    public List<Goods> listAlarm() {
        return goodsRepository.listAlarm();
    }
}
