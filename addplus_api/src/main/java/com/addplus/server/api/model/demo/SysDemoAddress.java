package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * demo地址
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/6 11:58 AM
 */
@Data
public class SysDemoAddress implements Serializable {
    private static final long serialVersionUID = -360489577307283923L;
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市名称
     */
    private String city;
}
