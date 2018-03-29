package com.halen.repository;

import com.halen.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Integer>, JpaSpecificationExecutor<RoleMenu> {

    @Query(value = "DELETE from t_role_menu WHERE role_id=?1", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteByRoleId(Integer roleId);
}
