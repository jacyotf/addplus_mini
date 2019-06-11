package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类名：SysUser
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:37
 * @describe 类描述：用户实体类
 */
@Data
public class SysUser extends BaseModel implements Serializable{

    private static final long serialVersionUID = 8555973391005922017L;

    /**
     * 用户名称
     */

    private String account;

    /**
     * 存储加密后的字符
     */
    private String password;


    /**
     * 存储加密的salt
     */
    private String passwordSalt;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户正式名称
     */
    private String name;

    /**
     * 0:男 1:女 2:其他
     */
    private Integer gender;

    /**
     * 出生年月日，格式“1992-10-4”
     */
    private Date birthday;

    /**
     * 多个地址使用；分隔开
     */
    private String address;

    /**
     * 所属多个组织使用,(逗号)分隔
     */
    private String oragnation;

    /**
     * 0:内部员工  1：外部员工
     */
    private Integer type;

    /**
     * 0：正®常  1：冻结
     */
    private Integer status;

    /**
     * 多个角色使用逗号（,）分隔
     */
    private String roles;

    /**
     * 用户手机号码，不能超过11位。不支持海外手机
     */
    private String phone;

    /**
     * 用户邮箱地址
     */
    private String email;

    /**
     * qq号码
     */
    private String qq;

    /**
     * 使用qq绑定的key
     */
    private String qqKey;

    /**
     * 微信号，使用微信号默认获取微信昵称
     */
    private String wechat;

    /**
     * 绑定微信的open_id
     */
    private String openId;

    /**
     * 每次登陆更新时间
     */
    private Date loginTime;

    /**
     * 登陆总次数，每登陆一次+1
     */
    private Integer loginCount;

    /**
     * 记录最后一次登陆地址
     */
    private String loginAddress;
}