package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.model.authority.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 类名: UserService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午4:25
 * @description 类描述: 用户业务接口类
 */
public interface UserService {

    /**
     * 方法描述：根据account获取用户
     *
     * @param account 账号名称
     * @return SysUser 用户信息
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     */
    SysUser selectByUsername(String account);

    /**
     * 方法描述：更新用户
     *
     * @param sysUser 更新用户信息内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     */
    Boolean updateLoginUser(SysUser sysUser) throws Exception;

    /**
     * 方法描述：增加用户
     *
     * @param sysUser 增加用户
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Boolean addUser(SysUser sysUser) throws Exception;

    /**
     * 方法描述：删除用户
     *
     * @param id 用户主键id
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Boolean deleteUserById(Long id) throws Exception;

    /**
     * 方法描述：根据id获取用户
     *
     * @param id 用户主键id
     * @return SysUser 用户信息实体
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_LOGIN_MEMBER_DISABLE
     */
    SysUser selectUserById(Long id) throws Exception;

    /**
     * 方法描述：根据id修改用户信息
     *
     * @param id 用户主键id
     * @return SysUser 用户信息实体
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    SysUser modifyUserGetInfoById(Long id) throws Exception;

    /**
     * 方法描述：加密字符串
     *
     * @param sysUser 用户信息
     * @return String 加密后密码内容
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    String encryptString(SysUser sysUser) throws Exception;

    /**
     * 方法描述：更新用户
     *
     * @param sysUser 更新用户内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Boolean updateUser(SysUser sysUser) throws Exception;

    /**
     * 方法描述：获取分页
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @return Page<SysUser> 分页获取用户列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    IPage<SysUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception;

    /**
     * 方法描述：获取用户
     *
     * @return SysUser 当前登录用户信息
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     */
    SysUser getByUser() throws Exception;

    /**
     * 方法描述：获取用户信息
     *
     * @param account 用户账号名称
     * @return Integer 账号名存在数量
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Integer getUserNameCount(String account) throws Exception;


}
