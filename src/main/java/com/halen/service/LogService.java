package com.halen.service;

import com.halen.entity.Log;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LogService {

    public void save(Log log);

    public List<Log> list(Log log, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    public Long getCount(Log log);
}
