package com.halen.service;

import com.halen.entity.OverflowList;
import com.halen.entity.OverflowListGoods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OverflowListService {

    public OverflowList findById(Integer id);

    public String getTodayMaxOverflowNumber();

    public void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList);

    public List<OverflowList> list(OverflowList overflowList, Sort.Direction direction, String...properties);
}
