package com.halen.repository;

import com.halen.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    @Query(value = "SELECT * FROM t_customer WHERE name like ?1", nativeQuery = true)
    public List<Customer> findByName(String name);
}
