package com.halen.service;

import com.halen.entity.CustomerReturnList;
import com.halen.entity.CustomerReturnListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomerReturnListService {

    public CustomerReturnList findById(Integer id);

    public String getTodayMaxCustomerReturnNumber();

    public void save(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> customerReturnListGoodsList);

    public List<CustomerReturnList> list(CustomerReturnList customerReturnList, Sort.Direction direction, String...properties);

    public void delete(Integer id);

    public void update(CustomerReturnList customerReturnList);
}
