package com.halen.service;

import com.halen.entity.Goods;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GoodsService {

    public List<Goods> findByTypeId(Integer typeId);

    public List<Goods> list(Goods goods, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    public Long getCount(Goods goods);

    public List<Goods> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    public Long getCountNoInventoryQuantityByCodeOrName(String codeOrName);

    public List<Goods> listHasInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    public Long getCountHasInventoryQuantityByCodeOrName(String codeOrName);

    public String getMaxGoodsCode();

    public void save(Goods goods);

    public Goods findById(Integer id);

    public void delete(Integer id);

    public List<Goods> listAlarm();
}
