package com.halen.service.impl;

import com.halen.entity.UserRole;
import com.halen.repository.UserRoleRepository;
import com.halen.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userRoleServiceImpl")
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleRepository userRoleRepository;

    @Override
    public void deleteByUserId(Integer userId) {
        userRoleRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        userRoleRepository.deleteByRoleId(roleId);
    }

    @Override
    public void save(UserRole userRole) {
        userRoleRepository.save(userRole);
    }
}
