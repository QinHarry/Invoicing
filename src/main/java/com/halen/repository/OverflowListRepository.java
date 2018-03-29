package com.halen.repository;

import com.halen.entity.OverflowList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OverflowListRepository extends JpaRepository<OverflowList, Integer>, JpaSpecificationExecutor<OverflowList> {

    @Query(value="SELECT MAX(overflow_number) FROM t_overflow_list WHERE TO_DAYS(overflow_date)=TO_DAYS(NOW())",nativeQuery=true)
    public String getTodayMaxOverflowNumber();
}
