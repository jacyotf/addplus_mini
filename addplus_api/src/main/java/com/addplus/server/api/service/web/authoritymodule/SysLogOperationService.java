package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.model.authority.SysLogOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 类名: SysLogOperationService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/04 10:15 AM
 * @description 类描述: 系统操作日志服务类
 */
public interface SysLogOperationService {

    /**
     * 方法描述：获取系统操作日志service
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @param logType 日志类型(0:普通 1:异常日志)
     * @param begin 开始时间
     * @param end 结束时间
     * @param methodName 方法名称
     * @param sort 0:正排序 1:倒排序
     * @param sortKey 排序key
     * @return 分页日志列表
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Page getListByPage(String begin, String end, Integer logType, String methodName, String module, Integer pageNo, Integer pageSize, Boolean sort, String sortKey) throws ErrorException;

    /**
     * 方法描述： 删除所有系统操作日志记录
     *
     * @return Boolean 是否成功
     * @throws Exception
     */
    Boolean deleteAllSysLogRecord() throws ErrorException;

    /**
     * 方法描述：根据主键id获取单条日志操作记录
     *
     * @param id 日志主键id
     * @return 日志内容详情
     * @throws ErrorException
     * @throws Exception
     */
    SysLogOperation getByPrimary(String id) throws ErrorException;
}
