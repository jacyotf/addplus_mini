package com.addplus.server.web.serviceimpl.web.commonmodule;


import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.mapper.authority.SysCommonProtocolMapper;
import com.addplus.server.api.model.authority.SysCommonProtocol;
import com.addplus.server.api.service.web.commonmodule.CommonProtocolService;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.addplus.server.web.baseserviceimpl.CommonProtocolBaseServiceImpl;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 程序协议通用表
 *
 * @author zhangjiehang
 * @date 2019-02-22 00:16:49
 */
@Service
public class CommonProtocolServiceImpl extends CommonProtocolBaseServiceImpl implements CommonProtocolService {

    @Autowired
    private RedisSlaveTemplateManager redisSlaveTemplateManager;

    @Autowired
    private SysCommonProtocolMapper sysCommonProtocolMapper;

    @Override
    @SysLogRecord
    public Integer updateByPrimaryExt(SysCommonProtocol entity) throws Exception {
        String protocolKey = sysCommonProtocolMapper.selectById(entity.getId()).getProtocolKey();
        redisSlaveTemplateManager.getMasterTemplate().delete(StringConstant.REDIS_PROTOCOLKEY_PREFIX + protocolKey);
        entity.setGmtModified(new Date());
        entity.setModifyUser(ShiroUtils.getUserAccount());
        return super.updateByPrimary(entity);
    }
}
