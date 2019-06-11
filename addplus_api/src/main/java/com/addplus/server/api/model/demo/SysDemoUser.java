package com.addplus.server.api.model.demo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * demo用户
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/6 12:00 PM
 */
@Data
public class SysDemoUser implements Serializable {
    private static final long serialVersionUID = 8095760544534811208L;
    /**
     * 用户主键id
     */
    @TableId
    private Integer id;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户是否删除(0:否 1:是)
     */
    @TableLogic
    private Integer deleted;
    /**
     * 数据版本
     */
    @Version
    private Integer version;
    /**
     * 地址信息，和用户是一对一的关系
     */
    private SysDemoAddress demoAddress;
    /**
     * 地址id
     */
    private Integer demoAddressId;
    /**
     * 用户拥有的车，和用户是一对多的关系
     */
    private List<SysDemoCar> cars;
    /**
     * 创建日期
     */
    private Date createDate;
}
