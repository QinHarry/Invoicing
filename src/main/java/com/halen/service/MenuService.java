package com.halen.service;

import com.halen.entity.Menu;

import java.util.List;

public interface MenuService {

    public Menu findById(Integer id);

    public List<Menu> findByParentIdAndRoleId(Integer parentId, Integer roleId);

    public List<Menu> findByRoleId(Integer roleId);

    public List<Menu> findByParentId(Integer parentId);
}
