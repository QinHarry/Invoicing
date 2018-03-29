package com.halen.repository;

import com.halen.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>, JpaSpecificationExecutor<Supplier> {

    @Query(value = "SELECT * FROM t_supplier WHERE name like ?1", nativeQuery = true)
    public List<Supplier> findByName(String name);
}
