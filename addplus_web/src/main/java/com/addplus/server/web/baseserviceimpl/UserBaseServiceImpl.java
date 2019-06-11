package com.addplus.server.web.baseserviceimpl;

import com.addplus.server.api.base.BaseServiceImpl;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysUser;

public class UserBaseServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> {

    @Override
    protected Class<SysUser> getClazz() {
        return SysUser.class;
    }
}
