package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * demo数据
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/6 12:00 PM
 */
@Data
public class SysDemoData implements Serializable {
    private static final long serialVersionUID = 8095760544534811208L;
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 数据名称
     */
    private String dataName;
}
