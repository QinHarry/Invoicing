package com.halen.service.impl;

import com.halen.entity.Log;
import com.halen.entity.User;
import com.halen.repository.LogRepository;
import com.halen.repository.UserRepository;
import com.halen.service.LogService;
import com.halen.util.StringUtil;
import org.apache.shiro.SecurityUtils;
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
import java.util.Date;
import java.util.List;

@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogRepository logRepository;

    @Resource
    private UserRepository userRepository;

    @Override
    public void save(Log log) {
        log.setTime(new Date());
        log.setUser(userRepository.findByUserName((String) SecurityUtils.getSubject().getPrincipal()));
        logRepository.save(log);
    }

    @Override
    public List<Log> list(Log log, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Log> pageLog = logRepository.findAll(new Specification<Log>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (log != null) {
                    if (!StringUtil.isEmpty(log.getType())) {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("type"), log.getType()));
                    }
                    if (log.getUser() != null && !StringUtil.isEmpty(log.getUser().getTrueName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
                    }
                    if (log.getBtime() != null) {
                        predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if (log.getEtime() != null) {
                        predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageLog.getContent();
    }

    @Override
    public Long getCount(Log log) {
        Long count = logRepository.count(new Specification<Log>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (log != null) {
                    if (!StringUtil.isEmpty(log.getType())) {
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("type"), log.getType()));
                    }
                    if (log.getUser() != null && !StringUtil.isEmpty(log.getUser().getTrueName())) {
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
                    }
                    if (log.getBtime() != null) {
                        predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if (log.getEtime() != null) {
                        predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}

