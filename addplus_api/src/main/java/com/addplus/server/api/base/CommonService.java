package com.addplus.server.api.base;

import java.util.Map;

/**
 * 类名: CommonService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/2/23 3:03 PM
 * @description 类描述: rest接口类公共请求类
 */
public interface CommonService {

    /**
     * 方法描述：初始化redis字典方法
     *
     * @author zhangjiehang
     * @date 2019/2/23 3:03 PM
     */
    void initRedisDataDictionary();

    /**
     * 方法描述：获取数据字典一个key里面hashMap
     *
     * @param key 缓存大key
     * @return Map<String , String>
     * @author zhangjiehang
     * @date 2019/2/23 3:03 PM
     */
    Map<String, String> getDictionaryByMapKey(String key);

    /**
     * 方法描述：获取数据字典的某一个key中内容
     *
     * @param key    key
     * @param stuKey 子级key
     * @return String
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/10/13 下午4:52
     */
    String getDictionaryByKey(String key, String stuKey);

    /**
     * 方法描述：获取用户的邀请码
     *
     * @return String 邀请码
     * @throws Exception
     * @author zhangjiehang
     * @date 2019/3/14 9:58 PM
     */
    String memberInviteCode();

    /**
     * 方法描述：订单号生成方法
     *
     * @param type  订单类型(1:购买vip,2:购买课程,3:购买训练营,4:购买训练营合集,5:购买专栏)
     * @param objId 对应主键id
     * @return String
     * @throws Exception
     * @author zhangjiehang
     * @date 2019/03/14 18:00
     */
    String getOrderSerialNumber(Integer type, Long objId) throws Exception;
}
