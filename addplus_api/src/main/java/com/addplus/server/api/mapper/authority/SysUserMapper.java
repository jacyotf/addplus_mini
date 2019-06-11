package com.addplus.server.api.mapper.authority;


import com.addplus.server.api.model.authority.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT\n" +
            " count(id) \n" +
            "FROM\n" +
            " sys_user \n" +
            "WHERE\n" +
            " account = #{account}")
    Integer getSysCountNum(@Param("account") String account);

}