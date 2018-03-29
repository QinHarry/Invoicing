package com.halen.repository;

import com.halen.entity.CustomerReturnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CustomerReturnListRepository extends JpaRepository<CustomerReturnList, Integer>, JpaSpecificationExecutor<CustomerReturnList> {

    @Query(value="SELECT MAX(customer_return_number) FROM t_customer_return_list WHERE TO_DAYS(customer_return_date)=TO_DAYS(NOW())",nativeQuery=true)
    public String getTodayMaxCustomerReturnNumber();
}
