package com.addplus.server.api.base;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.utils.DataUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    protected abstract Class<T> getClazz();

    private String idKey = "id";

    @Override
    public IPage<T> getListByPage(String filter, String filterKey, Integer pageNo, Integer pageSize, String search, String searchKey, Boolean sort, String sortKey) throws ErrorException {
        if (DataUtils.EmptyOrNegativeOrZero(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sortKey)) {
            queryWrapper.orderBy(true, sort, sortKey);
        }
        if (StringUtils.isNoneBlank(searchKey, search)) {
            String[] searchKeyArray = searchKey.split(",");
            for (int i = 0; i < searchKeyArray.length; i++) {
                queryWrapper.like(searchKeyArray[i], search).or(i < searchKeyArray.length - 1);
            }
        }
        if (StringUtils.isNoneBlank(filterKey, filter)) {
            String[] filterKeyArray = filterKey.split(",");
            String[] filterValueArray = filter.split(";");
            if (filterKeyArray.length == filterValueArray.length) {
                for (int i = 0; i < filterKeyArray.length; i++) {
                    String key = filterKeyArray[i];
                    queryWrapper.in(key, Arrays.asList(filterValueArray[i].split(",")));
                }
            }
        }
        Page<T> page = new Page<>(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public T getByPrimary(Long id) throws ErrorException {
        if (DataUtils.EmptyOrNegativeOrZero(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        return super.getById(id);
    }

    @Override
    public Integer add(T entity) throws ErrorException {
        if (entity instanceof Map) {
            Map<String, Object> paramsMap = (Map) entity;
        }
        String bodyText = JSON.toJSONString(entity);
        T body = JSONObject.parseObject(bodyText, getClazz());
        if (body != null) {
            return baseMapper.insert(body);
        }
        return 0;
    }

    @Override
    public Integer updateByPrimary(T entity) throws ErrorException {
        Map paramsMap = this.checkAndGetPrimary(entity);
        String bodyText = JSON.toJSONString(paramsMap);
        T body = JSONObject.parseObject(bodyText, getClazz());
        if (body != null) {
            return baseMapper.updateById(body);
        }
        return 0;
    }

    @Override
    public Integer deleteByPrimary(T entity) throws ErrorException {
        Object id = this.checkAndGetPrimary(entity).get(idKey);
        return baseMapper.deleteById(Long.parseLong(id.toString()));
    }

    protected T getBody(T entity) {
        String bodyText = JSON.toJSONString(entity);
        return JSONObject.parseObject(bodyText, getClazz());
    }

    protected Map checkAndGetPrimary(T entity) throws ErrorException {
        if (entity instanceof Map) {
            Map paramsMap = (Map) entity;
            if (DataUtils.isEmptyObject(paramsMap.get(idKey))) {
                throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
            }
            return paramsMap;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
    }
}
