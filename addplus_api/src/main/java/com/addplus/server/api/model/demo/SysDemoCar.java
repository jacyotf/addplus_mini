package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * demo车数据
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/6 11:59 AM
 * @description 类描述:
 */
@Data
public class SysDemoCar implements Serializable {
    private static final long serialVersionUID = -4938705749057952542L;
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 颜色
     */
    private String color;
    /**
     * 名称
     */
    private String name;
    /**
     * 关联用户主键id
     */
    private Long demoUserId;
}
