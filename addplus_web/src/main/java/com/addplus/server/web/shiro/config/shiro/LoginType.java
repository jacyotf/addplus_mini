package com.addplus.server.web.shiro.config.shiro;

/**
 * 类名: LoginType
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/10/27 4:32 PM
 * @description 类描述: 登录类型枚举类
 */
//普通用户登录，管理员登录
public enum LoginType {
    // 后台用户
    ADMIN("admin", 0),
    // 教师
    OTHER("other", 1);

    private Integer type;

    private String name;

    LoginType(String name, Integer type) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
