package com.addplus.server.consumer.rabbitmq;

import com.addplus.server.api.mapper.authority.SysLogOperationMapper;
import com.addplus.server.api.mapper.authority.SysRestMapperMapper;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysLogOperation;
import com.addplus.server.api.model.authority.SysLogOperationNormal;
import com.addplus.server.api.model.authority.SysRestMapper;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.mongodao.SysLogOperationDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = "order_queue")
public class LogOperatorConsumer{

    private final static Logger log = LoggerFactory.getLogger(LogOperatorConsumer.class);

    private final Map<String, String> restMapperMap = new HashMap<String, String>();

    @Value("${log.local}")
    private Boolean isLocal;

    @Value("${log.storage}")
    private Integer storageType;

    @Autowired(required = false)
    private SysLogOperationDao sysLogOperationDao;

    @Autowired
    private SysLogOperationMapper sysLogOperationMapper;

    @Autowired
    private SysRestMapperMapper sysRestMapperMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private void initRestMapperMap() {
        this.restMapperMap.clear();
        List<SysRestMapper> sysRestMapperList = sysRestMapperMapper.selectList(null);
        if (sysRestMapperList != null && sysRestMapperList.size() > 0) {
            sysRestMapperList.forEach(o -> {
                restMapperMap.put(o.getRestUrl(), o.getRestName());
            });
        }

    }

    @RabbitHandler
    public void logRecordOperator(JSONObject map) {

        SysLogOperation logOperation = new SysLogOperation();
        Object paramObj = map.get("param");
        if (paramObj != null) {
            Object[] param = (Object[]) paramObj;
            Object paramNameObj = map.get("paramName");
            if (paramNameObj != null) {
                String[] paramName = (String[]) paramNameObj;
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < param.length; i++) {
                    jsonObject.put(paramName[i], param[i]);
                }
                if (jsonObject != null && !jsonObject.isEmpty()) {
                    logOperation.setParam(JSON.toJSONString(jsonObject));
                }
            }
        }
        logOperation.setMethod(map.get("method").toString());
        logOperation.setService(map.get("service").toString());
        String methodNameKey = logOperation.getService() + "." + logOperation.getMethod();
        if (restMapperMap.containsKey(methodNameKey)) {
            logOperation.setMethodName(restMapperMap.get(methodNameKey));
        }
        logOperation.setModule(map.get("module").toString());
        Object memId = map.get("memId");
        if (memId != null) {
            logOperation.setMemId(memId.toString());
            if ("rest".equals(logOperation.getModule())) {
                // 插入当前的用户表格的账号

            } else {
                String modifyUser = map.getString("modifyUser");
                if (StringUtils.isBlank(modifyUser)) {
                    SysUser sysUser = sysUserMapper.selectById(memId.toString());
                    if (sysUser != null) {
                        logOperation.setModifyUser(sysUser.getAccount());
                    }
                } else {
                    logOperation.setModifyUser(modifyUser);
                }
            }
        }
        Object loginType = map.get("loginType");
        if (loginType != null) {
            logOperation.setLoginType(loginType.toString());
        }
        Object resultObj = map.get("result");
        if (resultObj != null) {
            logOperation.setResult(JSON.toJSONString(resultObj));
        }
        logOperation.setGmtCreate(new Date());
        logOperation.setLogType(map.getInteger("logType"));
        if (!isLocal) {
            if (storageType == 1) {
                SysLogOperationNormal sysLogOperationNormal = new SysLogOperationNormal();
                sysLogOperationNormal.setIsDeleted(0);
                BeanUtils.copyProperties(logOperation,sysLogOperationNormal);
                sysLogOperationMapper.insert(sysLogOperationNormal);
            } else if (storageType == 2) {
                sysLogOperationDao.insert(logOperation);
            }
        } else {
            log.info("rest invoke record :" + JSON.toJSONString(logOperation));
        }
    }
}
