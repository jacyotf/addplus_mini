package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuElement;

import java.util.List;

/**
 * 类名: MenuElementService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/6 上午10:07
 * @description 类描述: 菜单按钮功能实现类
 */
public interface MenuElementService {

    /**
     * 方法描述：根据用户id获取菜单列表
     *
     * @param userId 用户主键id
     * @return List<String> 用户拥有的内容
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_ERROR_PARAM
     */
    List<String> getUserFunctionList(Integer userId) throws Exception;

    /**
     * 方法描述：获取菜单元素列表
     *
     * @return List<SysMenuElement> 菜单列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_ERROR_PARAM
     */
    List<SysMenuElement> getMenuElementList() throws Exception;

    /**
     * 方法描述：根据菜单id获取菜单元素列表
     *
     * @param mId 菜单主键id
     * @return List<SysMenuElement> 菜单元素列表
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_ERROR_PARAM
     */
    List<SysMenuElement> getMenuElementByfunctionId(Integer mId) throws Exception;

    /**
     * 方法描述：根据id获取单个菜单元素
     *
     * @param eId 菜单主键id
     * @return SysMenuElement 元素详情
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_ERROR_PARAM
     */
    SysMenuElement getMenuElementById(Long eId)throws Exception;

    /**
     * 方法描述：增加菜单元素
     *
     * @param sysMenuElement 菜单元素内容
     * @return Boolean 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_NULLDATA
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean addMenuElement(SysMenuElement sysMenuElement)throws Exception;

    /**
     * 方法描述：根据id删除单个菜单元素
     *
     * @param eId 元素主键id
     * @return Boolean 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean deleteMenuElementById(Long eId)throws Exception;

    /**
     * 方法描述：更新单个菜单元素
     *
     * @param sysMenuElement 菜单元素内容
     * @return Boolean 是否成功(0:否 1:是)
     * @author zhangjiehang
     * @date 2019/3/08 12:14 AM
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    Boolean updateMenuElementById(SysMenuElement sysMenuElement)throws Exception;

}
