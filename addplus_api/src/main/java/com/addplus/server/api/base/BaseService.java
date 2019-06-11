package com.addplus.server.api.base;

import com.addplus.server.api.exception.ErrorException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseService<T> extends IService<T> {

    IPage<T> getListByPage(String filter, String filterKey, Integer pageNo, Integer pageSize, String search, String searchKey, Boolean sort, String sortKey) throws ErrorException;

    T getByPrimary(Long primary) throws ErrorException;

    Integer add(T entity) throws ErrorException;

    Integer updateByPrimary(T entity) throws ErrorException;

    Integer deleteByPrimary(T entity) throws ErrorException;
}
