package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.ext.PasswordParam;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 类名: UserAuthorityService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午4:25
 * @description 类描述: 用户权限业务接口类
 */
public interface UserAuthorityService {

    /**
     * 方法描述：获取用户拥有菜单列表
     *
     * @return List<SysMenuFunctionUser> 用户菜单列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    List<SysMenuFunctionUser> getUserMenuFunctionByUserId() throws Exception;

    /**
     * 方法描述：获取用户对应菜单元素列表
     *
     * @param mId 菜单主键id
     * @return List<SysMenuElement> 用户菜单元素列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    List<SysMenuElement> getUserMenuElemnetList(Integer mId) throws Exception;

    /**
     * 方法描述：获取当前用户信息
     *
     * @return JSONObject 用户信息
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     */
    JSONObject getByUser() throws Exception;

    /**
     * 修改当前登录用户密码
     *
     * @param passwordParam 密码参数
     * @return Integer 是否成功(0:否 1:是)
     * @author fuyq
     * @date 2019/03/12
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_LOGIN_UNAUTHORITY
     * @exception SYS_LOGIN_CONFIRM_PASSWORD_ERROR
     */
    Integer changePassword(PasswordParam passwordParam) throws Exception;


}
