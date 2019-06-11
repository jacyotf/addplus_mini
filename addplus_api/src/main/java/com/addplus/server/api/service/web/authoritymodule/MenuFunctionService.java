package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 类名: MenuService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 上午11:35
 * @description 类描述: 菜单的增删改查
 */
public interface MenuFunctionService {

    /**
     * 方法描述：根据id获取菜单
     *
     * @param id 菜单主键id
     * @return SysMenuFunction 菜单内容
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    SysMenuFunction getMenuFunction(Long id) throws Exception;

    /**
     * 方法描述：获取菜单关联分页根据类型
     *
     * @param pageNo 当前页 当前页
     * @param pageSize 当前页总数 当前页总数
     * @param type 类型(0:内部 1:外部)
     * @return Page<SysMenuFunctionUser> 分页获取用户菜单
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysMenuFunctionUser> getMenuFunctionByPage(Integer pageNo, Integer pageSize, Integer type) throws Exception;

    /**
     * 方法描述：获取菜单分页根据类型
     *
     * @param pageNo 当前页 当前页
     * @param pageSize 当前页总数 当前页总数
     * @param type 类型(0:内部 1:外部)
     * @return Page<SysMenuFunctionUser> 分页获取用户菜单
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysMenuFunctionUser> getMenuPageByType(Integer pageNo, Integer pageSize, Integer type) throws Exception;

    /**
     * 方法描述：根据角色id获取分页
     *
     * @param pageNo 当前页 当前页
     * @param pageSize 当前页总数 当前页总数
     * @param roleId 角色主键id
     * @return Page<SysRoleMenuFunctionUser> 角色菜单关联列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysRoleMenuFunctionUser> getMenuFunctionByPageWithRole(Integer pageNo, Integer pageSize, Long roleId) throws Exception;

    /**
     * 方法描述：更新菜单
     *
     * @param sysMenuFunction  角色菜单实体类
     * @return Boolean 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean updateMenuFunctionById(SysMenuFunction sysMenuFunction) throws Exception;

    /**
     * 方法描述：删除菜单
     *
     * @param id 菜单主键id
     * @return Boolean 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NOT_AVAILABLE
     */
    Boolean deleteMenuFunctionById(Integer id) throws Exception;

    /**
     * 方法描述：增加菜单
     *
     * @param sysMenuFunction 角色菜单实体类
     * @return SysMenuFunction 菜单实体类
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    SysMenuFunction addMenuFunction(SysMenuFunction sysMenuFunction) throws Exception;

    /**
     * 方法描述：根据条件获取菜单分页
     *
     * @param name 菜单名称
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @param pid 父菜单主键id
     * @param type 类型(0:内部 1:外部)
     * @return Page<SysMenuFunction> 菜单关联列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysMenuFunction> searchMenuFunctionByName(String name, Integer pageNo, Integer pageSize, String pid, Integer type) throws Exception;

    /**
     * 方法描述：根据分页获取菜单分页
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @return Page<SysMenuFunction> 菜单关联列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page<SysMenuFunction> getMenuFunctionListByPage(Integer pageNo, Integer pageSize) throws Exception;

    /**
     * 方法描述：刷新菜单
     *
     * @return Integer 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     */
    Integer refreshMenu();

    /**
     * 方法描述：刷新菜单权限
     *
     * @return Boolean 是否成功(false:否 true:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @throws Exception
     *
     */
    Boolean refreshAuthority() throws Exception;

}
