package com.halen.service;

import com.halen.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    public User findByUsername(String username);

    public List<User> list(User user, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    public Long getCount(User user);

    public void save(User user);

    public void delete(Integer id);

    public User findById(Integer id);
}
