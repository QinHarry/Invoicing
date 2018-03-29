package com.halen.repository;

import com.halen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Query(value = "SELECT * FROM t_user WHERE username=?1", nativeQuery = true)
    public User findByUserName(String username);
}
