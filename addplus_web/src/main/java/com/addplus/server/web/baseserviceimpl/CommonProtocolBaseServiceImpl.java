package com.addplus.server.web.baseserviceimpl;


import com.addplus.server.api.base.BaseServiceImpl;
import com.addplus.server.api.mapper.authority.SysCommonProtocolMapper;
import com.addplus.server.api.model.authority.SysCommonProtocol;

/**
 * 程序协议通用表
 *
 * @author zhangjiehang
 * @date 2019-02-22 00:06:17
 */
public class CommonProtocolBaseServiceImpl extends BaseServiceImpl<SysCommonProtocolMapper, SysCommonProtocol> {

    @Override
    protected Class<SysCommonProtocol> getClazz() {
        return SysCommonProtocol.class;
    }
}
