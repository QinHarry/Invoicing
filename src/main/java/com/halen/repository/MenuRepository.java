package com.halen.repository;

import com.halen.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query(value = "SELECT m.* FROM t_role r, t_role_menu rm, t_menu m WHERE rm.`role_id`=r.`id` AND rm.`menu_id`=m.`id` AND r.`id`=?1", nativeQuery = true)
    public List<Menu> findByRoleId(Integer roleId);

    @Query(value = "SELECT * from t_menu WHERE p_id=?1 AND id IN (SELECT menu_id FROM t_role_menu WHERE role_id=?2)", nativeQuery = true)
    public List<Menu> findByParentIdAndRoleId(Integer parentId, Integer roleId);

    @Query(value = "SELECT * FROM t_menu WHERE p_id=?1", nativeQuery = true)
    public List<Menu> findByParentId(Integer parentId);

    public Optional<Menu> findById(Integer id);
}
