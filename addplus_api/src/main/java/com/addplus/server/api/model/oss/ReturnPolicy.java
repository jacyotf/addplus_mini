package com.addplus.server.api.model.oss;

public class ReturnPolicy {
    private Object dataSet;
    private String errorInfo;
    private String returnCode;

    public ReturnPolicy() {
    }

    public ReturnPolicy(String returnCode, Object dataSet) {
        this.dataSet = dataSet;
        this.returnCode = returnCode;
    }

    public Object getDataSet() {
        return dataSet;
    }

    public void setDataSet(Object dataSet) {
        this.dataSet = dataSet;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
}
