package com.halen.repository;

import com.halen.entity.DamageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DamageListRepository extends JpaRepository<DamageList, Integer>, JpaSpecificationExecutor<DamageList> {

    @Query(value="SELECT MAX(damage_number) FROM t_damage_list WHERE TO_DAYS(damage_date)=TO_DAYS(NOW())",nativeQuery=true)
    public String getTodayMaxDamageNumber();
}
