package com.halen.service;

import com.halen.entity.RoleMenu;

public interface RoleMenuService {

    public void deleteByRoleId(Integer roleId);

    public void save(RoleMenu roleMenu);
}

