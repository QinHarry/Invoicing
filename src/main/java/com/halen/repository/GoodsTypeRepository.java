package com.halen.repository;

import com.halen.entity.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer>, JpaSpecificationExecutor<GoodsType> {
    @Query(value = "SELECT * FROM t_goodstype WHERE p_id=?1", nativeQuery = true)
    public List<GoodsType> findByParentId(Integer parentId);

}
