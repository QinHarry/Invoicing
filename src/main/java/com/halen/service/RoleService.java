package com.halen.service;

import com.halen.entity.Role;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RoleService {

    public List<Role> findByUserId(Integer userId);

    public Role findById(Integer id);

    public List<Role> listAll();

    public List<Role> list(Role role, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    public Long getCount(Role role);

    public void save(Role role);

    public void delete(Integer id);

    public Role findByRoleName(String roleName);
}
