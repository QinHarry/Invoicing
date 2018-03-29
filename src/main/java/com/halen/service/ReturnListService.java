package com.halen.service;

import com.halen.entity.ReturnList;
import com.halen.entity.ReturnListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReturnListService {

    public ReturnList findById(Integer id);

    public String getTodayMaxReturnNumber();

    public void save(ReturnList returnList, List<ReturnListGoods> returnListGoodsList);

    public List<ReturnList> list(ReturnList returnList, Sort.Direction direction, String...properties);

    public void delete(Integer id);

    public void update(ReturnList returnList);
}
