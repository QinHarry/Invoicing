package com.halen.service;

import com.halen.entity.Supplier;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SupplierService {

    public List<Supplier> list(Supplier supplier, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    public Long getCount(Supplier supplier);

    public void save(Supplier supplier);

    public void delete(Integer id);

    public Supplier findById(Integer id);

    public List<Supplier> findByName(String name);
}
