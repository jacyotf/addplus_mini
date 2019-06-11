package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.model.authority.SysExportRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 类名: SysExportRecordService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/04 10:15 AM
 * @description 类描述:导出excel报表服务类
 */
public interface SysExportRecordService extends IService<SysExportRecord> {
    /**
     * 获取导出记录
     *
     * @param pageNo 当前页
     * @param pageSize 当前页总数
     * @return IPage<SysExportRecord> excel文件列表
     * @author fuyq
     * @date 2019/03/05
     * @throws Exception
     */
    IPage<SysExportRecord> getListByPage(Integer pageNo, Integer pageSize) throws Exception;

    /**
     * 根据主键获取记录
     *
     * @param id 主键
     * @return SysExportRecord excel详情
     * @author fuyq
     * @date 2019/03/06
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    SysExportRecord getExportRecordById(Long id) throws Exception;

    /**
     * 添加导出记录
     *
     * @param sysExportRecord 导出记录
     * @return Integer 是否成功(0:否 1:是)
     * @author fuyq
     * @date 2019/03/05
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Integer insertExportRecord(SysExportRecord sysExportRecord) throws Exception;

    /**
     * 更新导出记录
     *
     * @param sysExportRecord 导出记录
     * @return Integer 是否成功(0:否 1:是)
     * @author fuyq
     * @date 2019/03/06
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    Integer updateExportRecord(SysExportRecord sysExportRecord) throws Exception;
}
