package com.addplus.server.api.service.rest.demomodule;

import com.addplus.server.api.model.demo.SysDemoUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 类名: HelloService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/6 12:02 PM
 * @description 类描述: Hello接口实例
 */



public interface HelloService {

    /**
     * 方法描述：模拟请求接口
     *
     * @param name 字符串
     * @return String 返回数据
     * @author zhangjiehang
     * @date 2018/10/10 下午2:21
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    String sayHello(String name) throws Exception;

    /**
     * 方法描述：添加用户
     *
     * @param user 用户实体类
     * @return SysDemoUser
     * @author zhangjiehang
     * @date 2018/10/10 下午2:21
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_DATABASEFAIL
     */
    SysDemoUser addUser(SysDemoUser user);

    /**
     * 方法描述：模拟使用Rabbitmq发送消息，业务是发送短信
     *
     * @param code 6位验证码
     * @param phone 手机号码
     * @return String 成功代码
     * @author zhangjiehang
     * @date 2019/3/6 12:10 PM
     * @throws Exception
     * @exception SYS_SUCCESS
     */
    String demoSendMessageByPhone(String code, String phone) throws Exception;


    /**
     * 方法描述：获取两个表关联一对一属性
     *
     * @param demoUserId demo用户主键id
     * @return SysDemoUser
     * @author zhangjiehang
     * @date 2019/3/6 1:23 PM
     * @throws Exception
     */
    SysDemoUser getDemoOneProperty(Integer demoUserId) throws Exception;

    /**
     * 方法描述：获取两个表一对多属性
     *
     * @param demoUserId demo用户主键id
     * @return SysDemoUser
     * @author zhangjiehang
     * @date 2019/3/6 1:23 PM
     * @throws Exception
     */
    SysDemoUser getDenoManyProperty(Integer demoUserId) throws Exception;

    /**
     * 方法描述：同时使用一对一和一对多属性
     *
     * @author zhangjiehang
     * @param demoUserId demo用户主键id
     * @return SysDemoUser 模拟用户实体类
     * @date 2018/9/23 下午4:19
     * @throws Exception
     */
    SysDemoUser getDenoOneManyProperty(Integer demoUserId,String aaaa) throws Exception;


    /**
     * 方法描述：获取全部demo对象，方式是读取xml文件
     *
     * @return List<SysDemoUser> 模拟用户列表
     * @author zhangjiehang
     * @date 2019/3/6 1:24 PM
     * @throws Exception
     */
    List<SysDemoUser> getAllDemoUser() throws Exception;



    List<SysDemoUser> getAllDemoUserByList(List<Integer> list) throws Exception;

    Boolean insertUser(SysDemoUser user)throws Exception;
    Boolean deleteUser(Integer demoUserId)throws Exception;

    SysDemoUser selectUser (Integer demoUserId)throws Exception;

    Boolean updateUser(Integer demoUserId)throws Exception;
    IPage<SysDemoUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception;

}
