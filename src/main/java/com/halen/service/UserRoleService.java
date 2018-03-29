package com.halen.service;

import com.halen.entity.UserRole;

public interface UserRoleService {

    public void deleteByUserId(Integer userId);

    public void save(UserRole userRole);

    public void deleteByRoleId(Integer roleId);
}
