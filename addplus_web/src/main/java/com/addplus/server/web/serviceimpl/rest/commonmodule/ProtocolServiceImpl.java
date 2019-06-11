package com.addplus.server.web.serviceimpl.rest.commonmodule;

import com.addplus.server.api.annotation.NotToken;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysCommonProtocolMapper;
import com.addplus.server.api.model.authority.SysCommonProtocol;
import com.addplus.server.api.service.rest.commonmodule.ProtocolService;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 类名: ProtocolServiceImpl
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/13 2:55 PM
 * @description 类描述: 通用协议接口实现类
 */
@Service
public class ProtocolServiceImpl implements ProtocolService {

    @Autowired
    private SysCommonProtocolMapper sysCommonProtocolMapper;

    @Autowired
    private RedisSlaveTemplateManager redisSlaveTemplateManager;

    @Override
    @NotToken
    public SysCommonProtocol getCommonProtocolByKey(String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        boolean isExit = redisSlaveTemplateManager.getSlaveTemlate().hasKey(StringConstant.REDIS_PROTOCOLKEY_PREFIX + key);
        if (isExit) {
                String context = (String) redisSlaveTemplateManager.getSlaveTemlate().opsForValue().get(StringConstant.REDIS_PROTOCOLKEY_PREFIX + key);
                if (StringUtils.isNotBlank(context)) {
                    return JSONObject.parseObject(context, SysCommonProtocol.class);
                }
        }
        QueryWrapper<SysCommonProtocol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("protocol_key", key);
        SysCommonProtocol kplCommonProtocol = sysCommonProtocolMapper.selectOne(queryWrapper);
        if (kplCommonProtocol == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        redisSlaveTemplateManager.getMasterTemplate().opsForValue().set(StringConstant.REDIS_PROTOCOLKEY_PREFIX + key, JSON.toJSONString(kplCommonProtocol), 10, TimeUnit.MINUTES);
        return kplCommonProtocol;
    }
}
