package com.halen.repository;

import com.halen.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {

    @Query(value = "DELETE FROM t_user_role WHERE user_id=?1", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteByUserId(Integer userId);

    @Query(value = "DELETE FROM t_user_role WHERE role_id=?1", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteByRoleId(Integer roleId);
}
