package com.halen.repository;

import com.halen.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogRepository extends JpaRepository<Log, Integer>, JpaSpecificationExecutor<Log> {
}
