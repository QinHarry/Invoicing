package com.halen.repository;

import com.halen.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Integer>, JpaSpecificationExecutor<Goods> {

    @Query(value = "SELECT * FROM t_goods WHERE type_id=?1", nativeQuery = true)
    public List<Goods> findByTypeId(Integer typeId);

    @Query(value = "SELECT MAX(code) FROM t_goods", nativeQuery = true)
    public String getMaxGoodsCode();

    @Query(value = "SELECT * FROM t_goods WHERE Inventory_quantity<min_num", nativeQuery = true)
    public List<Goods> listAlarm();
}
