package com.addplus.server.web.baseserviceimpl;

import com.addplus.server.api.base.CommonService;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysDataDictionaryMapper;
import com.addplus.server.api.model.authority.SysDataDictionary;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.api.utils.date.DateUtils;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 类名: CommonServiceImpl
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/2/23 3:13 PM
 * @description 类描述: 通用查询类
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private RedisSlaveTemplateManager redisSlaveTemplateManager;

    @Autowired
    private SysDataDictionaryMapper sysDataDictionaryMapper;


    @Override
    public void initRedisDataDictionary() {
        QueryWrapper<SysDataDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("redis_key", "redis_value");
        List<SysDataDictionary> dataDictionaryList = sysDataDictionaryMapper.selectList(queryWrapper);
        if (dataDictionaryList == null || dataDictionaryList.isEmpty()) {
            return;
        }
        dataDictionaryList.stream().filter(o -> Objects.nonNull(o)).forEach(o -> {
            if (redisSlaveTemplateManager.getMasterTemplate().hasKey(o.getRedisKey())) {
                redisSlaveTemplateManager.getMasterTemplate().delete(o.getRedisKey());
            }
            redisSlaveTemplateManager.getMasterTemplate().opsForHash().putAll(StringConstant.REDIS_DATA_DICTIONARY + o.getRedisKey(),
                    JSON.parseObject(o.getRedisValue(), Map.class));

        });
    }

    @Override
    public Map<String, String> getDictionaryByMapKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Map<String, String> stringMap = redisSlaveTemplateManager.getSlaveTemlate().opsForHash().entries(StringConstant.REDIS_DATA_DICTIONARY + key);
        if (stringMap == null || stringMap.isEmpty()) {
            return null;
        }
        return stringMap;
    }

    @Override
    public String getDictionaryByKey(String key, String stuKey) {
        if (DataUtils.isEmpty(key, stuKey)) {
            return null;
        }
        String redisValue = (String) redisSlaveTemplateManager.getSlaveTemlate().opsForHash().get(StringConstant.REDIS_DATA_DICTIONARY + key, stuKey);
        if (StringUtils.isBlank(redisValue)) {
            return null;
        }
        return redisValue;
    }
    @Override
    public String memberInviteCode() {
        boolean isExit = redisSlaveTemplateManager.getSlaveTemlate().hasKey(StringConstant.INVITE_CODE);
        long invoteCode = 0L;
        if (!isExit) {
            invoteCode = redisSlaveTemplateManager.getMasterTemplate().opsForValue().increment(StringConstant.INVITE_CODE, 100000);
        } else {
            invoteCode = redisSlaveTemplateManager.getMasterTemplate().opsForValue().increment(StringConstant.INVITE_CODE, 1);

        }
        return String.valueOf(invoteCode);
    }


    @Override
    public String getOrderSerialNumber(Integer type, Long objId) throws Exception {
        if (DataUtils.EmptyOrNegative(type) || DataUtils.EmptyOrNegative(objId)) {
            return null;
        }
        StringJoiner result = new StringJoiner("");
        result.add(DateUtils.getLocalDateTime());
        result.add(type.toString());
        String redisContext = MessageFormat.format(StringConstant.ORDER_NUM, type);
        result.add(String.format("%06x", objId));
        boolean isExit = redisSlaveTemplateManager.getSlaveTemlate().hasKey(redisContext);
        long orderNum = 0L;
        if (!isExit) {
            orderNum = redisSlaveTemplateManager.getMasterTemplate().opsForValue().increment(redisContext, 1000000);
        } else {
            orderNum = redisSlaveTemplateManager.getMasterTemplate().opsForValue().increment(redisContext, 1);
        }
        result.add(String.valueOf(orderNum));
        return result.toString();
    }
}
