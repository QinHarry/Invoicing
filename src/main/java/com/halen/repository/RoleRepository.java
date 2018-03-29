package com.halen.repository;

import com.halen.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    @Query(value = "SELECT r.* FROM t_user u, t_role r, t_user_role ur WHERE ur.`user_id`=u.`id` AND ur.`role_id`=r.`id` AND u.`id`=?1", nativeQuery = true)
    public List<Role> findByUserId(Integer userId);

    @Query(value = "SELECT * FROM t_role WHERE name=?1", nativeQuery = true)
    public Role findByRoleName(String roleName);
}
