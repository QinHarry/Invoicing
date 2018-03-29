package com.halen.service.impl;

import com.halen.entity.RoleMenu;
import com.halen.repository.RoleMenuRepository;
import com.halen.service.RoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("roleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuRepository roleMenuRepository;

    @Override
    public void deleteByRoleId(Integer roleId) {
        roleMenuRepository.deleteByRoleId(roleId);
    }

    @Override
    public void save(RoleMenu roleMenu) {
        roleMenuRepository.save(roleMenu);
    }
}
