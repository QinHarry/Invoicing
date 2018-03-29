package com.halen.repository;

import com.halen.entity.DamageListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DamageListGoodsRepository extends JpaRepository<DamageListGoods, Integer>, JpaSpecificationExecutor<DamageListGoods> {

    @Query(value="SELECT * FROM t_damage_list_goods WHERE damage_list_id=?1",nativeQuery=true)
    public List<DamageListGoods> listByDamageListId(Integer damageListId);

}
