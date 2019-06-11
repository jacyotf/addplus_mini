package com.addplus.server.api.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author fuyq
 * @date 2019/2/8
 */
public interface CommonMapper extends BaseMapper {

    @Select("SELECT ${fieldName} FROM ${tableName} WHERE is_deleted = 0")
    List<Map<String, Object>> getCommonOptions(@Param("tableName") String t, @Param("fieldName") String f);
}
