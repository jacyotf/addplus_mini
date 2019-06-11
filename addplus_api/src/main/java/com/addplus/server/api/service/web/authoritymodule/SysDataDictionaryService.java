package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.model.authority.ext.SysDataDictionaryExt;

import java.util.List;

/**
 * 类名: SysDataDictionaryService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/3/04 10:15 AM
 * @description 类描述:数据字典服务接口类
 */
public interface SysDataDictionaryService {

    /**
     * 方法描述：获取所有redisKey列表
     *
     * @return redis key列表
     * @throws Exception
     * @exception SYS_ERROR_NULLDATA
     */
    List<String> getRedisKeyList() throws Exception;

    /**
     * 方法描述：根据redisKey获取整个值的内容
     *
     * @author zhangjiehang
     * @param redisKey key值
     * @return SysDataDictionaryExt 当前key值内容详情
     * @date 2019/3/05 下午1:55
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    SysDataDictionaryExt getDataDictionaryValue(String redisKey) throws Exception;

    /**
     * 方法描述：添加数据整个内容值
     * @author zhangjiehang
     * @param dataDictionary key值内容
     * @return Boolean 是否成功(0:否 1:是)
     * @date 2019/3/05 下午3:26
     * @throws Exception
     * @exception SYS_ERROR_PARAM
     * @exception SYS_ERROR_NULLDATA
     */
    Boolean addDataDictionaryByKey(SysDataDictionaryExt dataDictionary) throws Exception;

    /**
     * 方法描述：更新单个value的信息
     *
     * @author zhangjiehang
     * @param dataDictionary key值内容
     * @return Boolean 是否成功(0:否 1:是)
     * @date 2019/3/05 下午3:26
     * @throws Exception
     */
    Boolean updateDataDictionarySingleValue(SysDataDictionaryExt dataDictionary) throws Exception;

    /**
     * 方法描述：删除当个数据字典内容
     *
     * @author zhangjiehang
     * @param redisKey 小key
     * @param key 大key
     * @return Boolean 是否成功(0:否 1:是)
     * @date 2019/3/5 下午4:40
     * @throws Exception
     */
    Boolean deleteDataDictionarySingleKey(String key, String redisKey) throws Exception;
}

