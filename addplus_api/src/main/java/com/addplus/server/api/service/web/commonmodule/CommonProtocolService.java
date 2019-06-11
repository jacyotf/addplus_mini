package com.addplus.server.api.service.web.commonmodule;

import com.addplus.server.api.baseservice.CommonProtocolBaseService;
import com.addplus.server.api.model.authority.SysCommonProtocol;

/**
 * 程序协议通用表
 *
 * @author zhangjiehang
 * @date 2019-02-22 00:06:17
 */
public interface CommonProtocolService extends CommonProtocolBaseService {

    /**
     * 方法描述：根据id修改更新单个协议内容，删除redis缓存
     * @param entity 协议内容
     * @return Integer 是否成功(0:否 1:是)
     * @throws Exception
     */
    Integer updateByPrimaryExt(SysCommonProtocol entity) throws Exception;
}

