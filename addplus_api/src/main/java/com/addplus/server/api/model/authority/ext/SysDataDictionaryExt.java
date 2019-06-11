package com.addplus.server.api.model.authority.ext;

import com.addplus.server.api.model.authority.SysDataDictionary;
import lombok.Data;

import java.util.List;

@Data
public class SysDataDictionaryExt extends SysDataDictionary implements Comparable<SysDataDictionaryExt> {

    /**
     * 解析redisValue（JSON）得到的list列表
     */
    List<SysDataDictionaryExt> redisValueList;

    /**
     * 更新操作：存放老键的字段
     */
    String oldKey;

    /**
     * redis 键
     */
    Object key;

    /**
     * redis 值
     */
    Object value;

    @Override
    public int compareTo(SysDataDictionaryExt o) {
        Object key = o.getKey();
        if (this.getKey().hashCode() > key.hashCode()) {
            return 1;
        } else if (this.getKey().hashCode() < key.hashCode()) {
            return -1;
        }
        return 0;
    }
}
