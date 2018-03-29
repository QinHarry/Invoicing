package com.halen.service.impl;

import com.halen.entity.User;
import com.halen.repository.UserRepository;
import com.halen.service.UserService;
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

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public List<User> list(User user, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> pageUser = userRepository.findAll(new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (user != null) {
                    if (!StringUtil.isEmpty(user.getUsername())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("username"), "%" + user.getUsername() + "%"));
                    }
                    predicate.getExpressions().add(criteriaBuilder.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        }, pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(User user) {
        Long count = userRepository.count(new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (user != null) {
                    if (!StringUtil.isEmpty(user.getUsername())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("username"), "%" + user.getUsername() + "%"));
                    }
                    predicate.getExpressions().add(criteriaBuilder.notEqual(root.get("id"), 1));
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }
}
