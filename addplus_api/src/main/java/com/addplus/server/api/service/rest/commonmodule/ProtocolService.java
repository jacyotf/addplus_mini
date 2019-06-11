package com.addplus.server.api.service.rest.commonmodule;

import com.addplus.server.api.model.authority.SysCommonProtocol;

/**
 * 类名: ProtocolService
 *
 * @author zhangjiehang
 * @date  
 * @version V1.0
 * @description 类描述: 通用协议服务类
 */
public interface ProtocolService {

    /**
      * 方法描述：获取通用协议内容接口
      *
      * @param key 协议key名称
      * @return KplCommonProtocol 通用协议实体类
      * @author zhangjiehang
      * @date 2019/3/13 2:53 PM
      * @throws Exception
      * @exception SYS_ERROR_PARAM
      * @exception SYS_ERROR_NULLDATA
      */
    SysCommonProtocol getCommonProtocolByKey(String key) throws Exception;
}
