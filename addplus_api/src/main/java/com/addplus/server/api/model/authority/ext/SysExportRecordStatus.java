package com.addplus.server.api.model.authority.ext;

/**
 * @author fuyq
 * @date 2019/4/15
 */
public enum SysExportRecordStatus {

    /**
     * 正在处理
     */
    PROCESSING(0),

    /**
     * 处理成功
     */
    SUCCESS(1),

    /**
     * 处理失败
     */
    ERROR(2);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    SysExportRecordStatus(Integer value) {
        this.value = value;
    }
}
