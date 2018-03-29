package com.halen.service;

import com.halen.entity.SaleList;
import com.halen.entity.SaleListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SaleListService {

    public SaleList findById(Integer id);

    public String getTodayMaxSaleNumber();

    public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList);

    public List<SaleList> list(SaleList saleList, Sort.Direction direction, String...properties);

    public void delete(Integer id);

    public void update(SaleList saleList);


    public List<Object> countSaleByDay(String begin,String end);

    public List<Object> countSaleByMonth(String begin,String end);
}
