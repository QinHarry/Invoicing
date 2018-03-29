package com.halen.repository;

import com.halen.entity.GoodsUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsUnitRepository extends JpaRepository<GoodsUnit, Integer>, JpaSpecificationExecutor<GoodsUnit> {
}
