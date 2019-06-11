package com.addplus.server.api.service.web.commonmodule;

import com.addplus.server.api.exception.ErrorException;

import java.util.List;
import java.util.Map;

/**
 * @author fuyq
 * @date 2019/2/8
 */
public interface CommonService {

    /**
     *  获取选项
     *
     * @param f value字段名，多个用英文逗号隔开
     * @param t 表名
     * @return List<Map<String, Object>>
     * @author fuyq
     * @date 2019/02/08
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     */
    List<Map<String, Object>> getCommonOptions(String f, String t) throws ErrorException;

    /**
     * 方法描述：获取数据字典一个key里面hashMap
     *
     * @param key 缓存大key
     * @return Map<String , String> 数据字典内容
     * @author wzh
     * @date 2019/4/8 15:03
     * @throws Exception
     */
    Map<String, String> getDictionaryByMapKey(String key);
}
