package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseMonogoModel;
import org.mongodb.morphia.annotations.Entity;

import java.io.Serializable;

/**
 * 类名：SysLogOperation
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:28
 * @describe 类描述：日志记录操作实体类
 */
@Entity("SysLogOperation")
public class SysLogOperation extends BaseMonogoModel implements Serializable {

    private static final long serialVersionUID = -8665253035862654123L;
    /**
     * 关联用户主键Id
     */
    private String memId;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 接口类
     */
    private String service;

    /**
     * 入参参数
     */
    private String param;

    /**
     * 结果
     */
    private String result;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 日志类型(0:业务日志 1:异常日志)
     */
    private Integer logType;

    /**
     * 方法名称
     */
    private String methodName;

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}