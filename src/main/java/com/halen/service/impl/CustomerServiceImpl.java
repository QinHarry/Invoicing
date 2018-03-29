package com.halen.service.impl;

import com.halen.entity.Customer;
import com.halen.repository.CustomerRepository;
import com.halen.service.CustomerService;
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

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> list(Customer customer, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page -1, pageSize);
        Page<Customer> pageCustomer = customerRepository.findAll(new Specification<Customer>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (customer != null) {
                    if (!StringUtil.isEmpty(customer.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + customer.getName() + "%"));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageCustomer.getContent();
    }

    @Override
    public Long getCount(Customer customer) {
        Long count = customerRepository.count(new Specification<Customer>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (customer != null) {
                    if (!StringUtil.isEmpty(customer.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + customer.getName() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }
}
