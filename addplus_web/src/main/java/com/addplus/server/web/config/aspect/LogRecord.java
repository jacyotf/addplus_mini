package com.addplus.server.web.config.aspect;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.modelenum.QueueEnum;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 类名: LogRecord
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/6/23 下午2:58
 * @description 类描述: 记录访问日志aop切面操作
 */
@Aspect
@Component
@ConditionalOnProperty(name = "log.record.operator", havingValue = "true")
public class LogRecord {

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.addplus.server.api.annotation.SysLogRecord)")
    public void LogPointCut() {
    }

    @AfterReturning(returning = "ret", pointcut = "LogPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        String module = (String) request.getAttribute(StringConstant.MODULE);
        JSONObject map = this.initJson(joinPoint, module);
        if (ret != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", ret);
            map.put("result", jsonObject);
        }
        map.put("logType", 0);
        rabbitTemplate.convertAndSend(QueueEnum.LOG_QUEUE.getExchange(), QueueEnum.LOG_QUEUE.getRoutingKey(), map);
    }

    @AfterThrowing(throwing = "e", pointcut = "LogPointCut()")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        String module = (String) request.getAttribute(StringConstant.MODULE);
        JSONObject map = this.initJson(joinPoint, module);
        if (e != null) {
            JSONObject jsonObject = new JSONObject();
            if (e instanceof ErrorException) {
                ErrorException errorException = (ErrorException) e;
                jsonObject.put("result", JSON.toJSONString(errorException));
                map.put("logType", 0);
            } else {
                jsonObject.put("result", e.toString());
                map.put("logType", 1);
            }
            map.put("result", jsonObject);
        } else {
            map.put("logType", 1);
        }
        rabbitTemplate.convertAndSend(QueueEnum.LOG_QUEUE.getExchange(), QueueEnum.LOG_QUEUE.getRoutingKey(), map);
    }


    private JSONObject initJson(JoinPoint joinPoint, String module) {
        JSONObject map = new JSONObject();
        //获取参数名称
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        if (paramNames != null && paramNames.length > 0) {
            map.put("paramName", paramNames);
        }
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            map.put("param", args);
        }
        map.put("service", joinPoint.getSignature().getDeclaringType().getInterfaces()[0].getName());
        map.put("method", joinPoint.getSignature().getName());
        if ("rest".equals(module)) {
            String memId = (String) request.getAttribute("memberId");
            if (StringUtils.isNotBlank(memId)) {
                map.put("memId", memId);
            }
            map.put("module", "rest");
        } else if ("web".equals(module)) {
            map.put("memId", ShiroUtils.getUserId());
            map.put("modifyUser", ShiroUtils.getUserAccount());
            map.put("loginType", ShiroUtils.getLoginType());
            map.put("module", "web");
        }
        return map;
    }


}
