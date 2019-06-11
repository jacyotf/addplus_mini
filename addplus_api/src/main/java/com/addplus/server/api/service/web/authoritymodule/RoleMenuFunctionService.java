package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysRoleMenuFunction;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionExt;

import java.util.List;

/**
 * 类名: RoleMenufunctionService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/11 下午1:48
 * @description 类描述: 角色-功能关联实现类
 */
public interface RoleMenuFunctionService {

    /**
     * 方法描述：根据id获取列表
     *
     * @param id 角色功能主键id
     * @return List<SysRoleMenuFunction> 当前角色拥有菜单列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    List<SysRoleMenuFunction> getRoleMenuByRoleId(Long id) throws Exception;

    /**
     * 方法描述：根据菜单和角色主键获取当前子菜单
     *
     * @param roleId 角色主键id
     * @param menuId 菜单主键id
     * @return List<SysRoleMenuFunction> 当前菜单子菜单主键id
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    List<SysRoleMenuFunction> getRoleMenuByRoleAndMenuId(Long roleId, Long menuId) throws Exception;

    /**
     * 方法描述：增加角色的菜单
     *
     * @param sysRoleMenufunction 菜单内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean addRoleMenufunction(SysRoleMenuFunction sysRoleMenufunction) throws Exception;

    /**
     * 方法描述：删除角色的菜单
     *
     * @param id 菜单关联主键id
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     * @exception SYS_ERROR_NOT_AVAILABLE
     */
    Boolean logicalDeletedRoleMenufunctionById(Long id) throws Exception;

    /**
     * 方法描述：更新角色的菜单
     *
     * @param sysRoleMenuFunctionExt 更新菜单内容
     * @return Boolean 是否成功
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Boolean saveRoleMenuFunction(SysRoleMenuFunctionExt sysRoleMenuFunctionExt) throws Exception;


}
