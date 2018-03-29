package com.halen.service;

import com.halen.entity.Customer;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomerService {

    public List<Customer> list(Customer customer, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    public Long getCount(Customer customer);

    public void save(Customer customer);

    public void delete(Integer id);

    public Customer findById(Integer id);

    public List<Customer> findByName(String name);
}
