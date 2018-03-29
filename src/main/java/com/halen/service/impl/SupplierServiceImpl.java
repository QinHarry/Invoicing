package com.halen.service.impl;

import com.halen.entity.Supplier;
import com.halen.repository.SupplierRepository;
import com.halen.service.SupplierService;
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

@Service("supplierService")
public class SupplierServiceImpl implements SupplierService {

    @Resource
    private SupplierRepository supplierRepository;

    @Override
    public List<Supplier> list(Supplier supplier, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page -1, pageSize);
        Page<Supplier> pageSupplier = supplierRepository.findAll(new Specification<Supplier>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (supplier != null) {
                    if (!StringUtil.isEmpty(supplier.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + supplier.getName() + "%"));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageSupplier.getContent();
    }

    @Override
    public Long getCount(Supplier supplier) {
        Long count = supplierRepository.count(new Specification<Supplier>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (supplier != null) {
                    if (!StringUtil.isEmpty(supplier.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + supplier.getName() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void save(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Override
    public void delete(Integer id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public Supplier findById(Integer id) {
        return supplierRepository.findById(id).get();
    }

    @Override
    public List<Supplier> findByName(String name) {
        return supplierRepository.findByName(name);
    }
}
