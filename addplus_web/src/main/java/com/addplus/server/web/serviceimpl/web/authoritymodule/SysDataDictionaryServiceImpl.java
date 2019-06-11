package com.addplus.server.web.serviceimpl.web.authoritymodule;

import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysDataDictionaryMapper;
import com.addplus.server.api.model.authority.SysDataDictionary;
import com.addplus.server.api.model.authority.ext.SysDataDictionaryExt;
import com.addplus.server.api.service.web.authoritymodule.SysDataDictionaryService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据字典基础表
 *
 * @author zhangjiehang
 * @date 2019-02-22 00:16:47
 */
@Service
public class SysDataDictionaryServiceImpl  implements SysDataDictionaryService {

    @Autowired
    RedisSlaveTemplateManager redisSlaveTemplateManager;

    @Autowired
    private SysDataDictionaryMapper sysDataDictionaryMapper;

    @Override
    public List<String> getRedisKeyList() throws Exception {
        QueryWrapper<SysDataDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("redis_key", "redis_value");
        List<SysDataDictionary> dataDictionaries = sysDataDictionaryMapper.selectList(queryWrapper);
        List<String> redisKeyList = dataDictionaries.stream().filter(o -> o.getRedisValue().contains(":")).map(o -> {
            SysDataDictionary dataDictionary = (SysDataDictionary) o;
            return dataDictionary.getRedisKey();
        }).collect(Collectors.toList());
        if (redisKeyList != null) {
            return redisKeyList;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
    }

    @Override
    public SysDataDictionaryExt getDataDictionaryValue(String redisKey) throws Exception {
        if (DataUtils.isEmpty(redisKey)) {
            return null;
        }
        QueryWrapper<SysDataDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("redis_key", redisKey);
        SysDataDictionary dataDictionary = sysDataDictionaryMapper.selectOne(queryWrapper);
        if (dataDictionary == null && dataDictionary.getRedisValue() != null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        Date gmtModified = dataDictionary.getGmtModified();
        String modifyUser = dataDictionary.getModifyUser();
        Map<String, String> map = (Map<String, String>) JSONObject.parse(dataDictionary.getRedisValue());
        SysDataDictionaryExt sysDataDictionaryExt = new SysDataDictionaryExt();
        sysDataDictionaryExt.setRedisKey(redisKey);
        List<SysDataDictionaryExt> redisValueList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            SysDataDictionaryExt temp = new SysDataDictionaryExt();
            if (DataUtils.isEmptyObject(entry.getKey())) {
                temp.setKey("--");
            } else {
                temp.setKey(entry.getKey());
            }
            if (DataUtils.isEmptyObject(entry.getValue())) {
                temp.setValue("--");
            } else {
                temp.setValue(entry.getValue());
            }
            temp.setGmtModified(gmtModified);
            temp.setModifyUser(modifyUser);
            redisValueList.add(temp);
        }
        Collections.sort(redisValueList);
        sysDataDictionaryExt.setRedisValueList(redisValueList);
        return sysDataDictionaryExt;
    }

    @Override
    @SysLogRecord
    public Boolean addDataDictionaryByKey(SysDataDictionaryExt dataDictionary) throws Exception {
        if (DataUtils.isEmptyObject(dataDictionary)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        String redisKey = dataDictionary.getRedisKey();
        String key = String.valueOf(dataDictionary.getKey());
        String value = String.valueOf(dataDictionary.getValue());
        if (key == null || key.equals("--")) {
            key = "";
        }
        if (value == null || value.equals("--")) {
            value = "";
        }
        Map<String, String> map = redisSlaveTemplateManager.getSlaveTemlate().opsForHash().entries(StringConstant.REDIS_DATA_DICTIONARY + redisKey);
        if (map == null || map.isEmpty()) {
            return null;
        }
        map.put(key, value);
        int update = update(map, redisKey);
        //缓存更新
        redisSlaveTemplateManager.getMasterTemplate().opsForHash().putAll(StringConstant.REDIS_DATA_DICTIONARY + redisKey, map);
        return update > 0;
    }

    @Override
    @SysLogRecord
    public Boolean updateDataDictionarySingleValue(SysDataDictionaryExt dataDictionary) throws Exception {
        if (DataUtils.isEmptyObject(dataDictionary, dataDictionary.getRedisKey(), dataDictionary.getOldKey())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        String key = String.valueOf(dataDictionary.getKey());
        String value = String.valueOf(dataDictionary.getValue());
        String redisKey = dataDictionary.getRedisKey();
        String oldKey = dataDictionary.getOldKey();
        if (key == null || key.equals("--")) {
            key = "";
        }
        if (oldKey == null || oldKey.equals("--")) {
            oldKey = "";
        }
        if (value == null || value.equals("--")) {
            value = "";
        }
        Map<String, String> map = redisSlaveTemplateManager.getSlaveTemlate().opsForHash().entries(StringConstant.REDIS_DATA_DICTIONARY + redisKey);
        if (map == null || map.isEmpty()) {
            return null;
        }
        if (!oldKey.equals(key)) {
            map.remove(oldKey);
            redisSlaveTemplateManager.getMasterTemplate().opsForHash().delete(StringConstant.REDIS_DATA_DICTIONARY + redisKey, oldKey);
        }
        map.put(key, value);
        int update = update(map, redisKey);
        //缓存更新
        redisSlaveTemplateManager.getMasterTemplate().opsForHash().putAll(StringConstant.REDIS_DATA_DICTIONARY + redisKey, map);
        return update > 0;
    }

    @Override
    @SysLogRecord
    public Boolean deleteDataDictionarySingleKey(String key, String redisKey) throws Exception {
        if (DataUtils.isEmpty(redisKey, key)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Map<String, String> map = redisSlaveTemplateManager.getSlaveTemlate().opsForHash().entries(StringConstant.REDIS_DATA_DICTIONARY + redisKey);
        if (map == null || map.isEmpty()) {
            return null;
        }
        map.remove(key);
        int update = update(map, redisKey);
        //缓存更新
        redisSlaveTemplateManager.getMasterTemplate().opsForHash().delete(StringConstant.REDIS_DATA_DICTIONARY + redisKey, key);
        return update > 0;
    }

    /**
     * 方法描述： 抽取同步数据库更新方法
     *
     * @param map
     * @param redisKey
     * @return
     * @throws ErrorException
     */
    private int update(Map<String, String> map, String redisKey) throws ErrorException {
        QueryWrapper<SysDataDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("redis_key", redisKey);
        SysDataDictionary sysDataDictionary = sysDataDictionaryMapper.selectOne(queryWrapper);
        if (sysDataDictionary == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        sysDataDictionary.setRedisValue(JSON.toJSONString(map));
        sysDataDictionary.setGmtModified(new Date());
        sysDataDictionary.setModifyUser(ShiroUtils.getUserAccount());
        int update = sysDataDictionaryMapper.updateById(sysDataDictionary);
        return update;
    }
}
