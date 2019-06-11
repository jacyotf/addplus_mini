package com.addplus.server.api.exception;

import com.addplus.server.api.constant.ErrorCode;
import com.alibaba.fastjson.JSON;

/**
 * 自定义异常
 */
public class ErrorException extends Exception
{
    
    private String returnCode;
    
    private Object returnInfo;

    private String errorInfo;

    private ErrorCode codeEnum;


    public ErrorException(ErrorCode codeEnum)
    {
        super(codeEnum.getCode()+"_"+codeEnum.getError());
        this.codeEnum = codeEnum;
        this.returnCode = codeEnum.getCode();
        this.errorInfo = codeEnum.getError();
    }

    public ErrorException(ErrorCode codeEnum, Object returnInfo)
    {
        super(codeEnum.getCode()+"_"+codeEnum.getError()+"_"+ JSON.toJSONString(returnInfo));
        this.codeEnum = codeEnum;
        this.returnCode = codeEnum.getCode();
        this.errorInfo = codeEnum.getError();
        this.returnInfo = returnInfo;
    }

    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Object getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(Object returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ErrorCode getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(ErrorCode codeEnum) {
        this.codeEnum = codeEnum;
    }
}
