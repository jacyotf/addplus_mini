package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.model.authority.SysRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 类名: RoleService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/04 10:15 AM
 * @description 类描述: 角色接口类
 */
public interface RoleService{

    /**
     * 方法描述：获取分页
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @param type 类型(0:内部 1:外部 2:全部)
     * @return Page<SysRole> 分页角色列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysRole> getRoleByPage(Integer pageNo, Integer pageSize, Integer type) throws Exception;

    /**
     * 方法描述：获取分页
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @return Page<SysRole> 分页角色列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysRole> getStoreRoleByPage(Integer pageNo, Integer pageSize) throws Exception;

    /**
     * 方法描述：根据rId获取
     *
     * @param rId 角色主键id
     * @return SysRole 角色内容
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    SysRole getRoleById(Long rId) throws Exception;

    /**
     * 方法描述：增加角色接口
     *
     * @param sysRole 增加角色内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean addRole(SysRole sysRole) throws Exception;

    /**
     * 方法描述：更新角色接口
     *
     * @param sysRole 更新角色内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     * @exception SYS_ERROR_NOT_AVAILABLE
     */
    Boolean updateRoleById(SysRole sysRole) throws Exception;

    /**
     * 方法描述：删除角色主键id
     *
     * @param rId 角色主键id
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     * @exception SYS_ERROR_NOT_AVAILABLE
     */
    Boolean logicallyDeleteRoleById(Long rId) throws Exception;
}
