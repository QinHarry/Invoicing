package com.halen.service.impl;

import com.halen.entity.Menu;
import com.halen.repository.MenuRepository;
import com.halen.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findByParentIdAndRoleId(Integer parentId, Integer roleId) {
        return menuRepository.findByParentIdAndRoleId(parentId, roleId);
    }

    @Override
    public List<Menu> findByRoleId(Integer roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    @Override
    public List<Menu> findByParentId(Integer parentId) {
        return menuRepository.findByParentId(parentId);
    }

    @Override
    public Menu findById(Integer id) {
        return menuRepository.findById(id).get();
    }
}
