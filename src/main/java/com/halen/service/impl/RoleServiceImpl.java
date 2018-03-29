package com.halen.service.impl;

import com.halen.entity.Role;
import com.halen.repository.RoleRepository;
import com.halen.service.RoleService;
import com.halen.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Override
    public List<Role> findByUserId(Integer userId) {
        return roleRepository.findByUserId(userId);
    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> list(Role role, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Role> pageUser = roleRepository.findAll(new Specification<Role>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (role != null) {
                    if (!StringUtil.isEmpty(role.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + role.getName() + "%"));
                    }
                    predicate.getExpressions().add(criteriaBuilder.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        }, pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(Role role) {
        Long count = roleRepository.count(new Specification<Role>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (role != null) {
                    if (!StringUtil.isEmpty(role.getName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + role.getName() + "%"));
                    }
                    predicate.getExpressions().add(criteriaBuilder.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
