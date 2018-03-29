package com.halen.repository;

import com.halen.entity.ReturnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ReturnListRepository extends JpaRepository<ReturnList, Integer>, JpaSpecificationExecutor<ReturnList> {

    @Query(value = "SELECT MAX(return_number) FROM t_return_list WHERE TO_DAYS(return_date)=TO_DAYS(NOW())", nativeQuery = true)
    public String getTodayMaxReturnNumber();
}
