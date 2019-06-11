package com.addplus.server.web.serviceimpl.web.authoritymodule;

import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysMenuElementMapper;
import com.addplus.server.api.mapper.authority.SysMenuFunctionMapper;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.model.authority.ext.PasswordParam;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.addplus.server.api.service.web.authoritymodule.UserAuthorityService;
import com.addplus.server.api.utils.MenuFunctionTreeUtils;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: UserAuthorityServiceImpl
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午4:40
 * @description 类描述: 用户权限实现类
 */
@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    private SysMenuFunctionMapper sysMenuFunctionMapper;

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DefaultHashService defaultHashService;


    @Override
    public List<SysMenuFunctionUser> getUserMenuFunctionByUserId() throws Exception {
        //把菜单信息存入用户的session
        String roleAll[] = null;
        roleAll = this.getByUserRoles().split(",");
        if (roleAll == null || roleAll.length == 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        } else {
            List<Integer> roles = new ArrayList<>();
            for (String r : roleAll) {
                roles.add(Integer.valueOf(r));
            }
            List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.getMenuFunctionUserAll(roles);
            if (sysMenuFunctionList == null || sysMenuFunctionList.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
            }
            List<SysMenuFunctionUser> menuTree = MenuFunctionTreeUtils.getMenuFunctionTree(sysMenuFunctionList);
            if (menuTree == null || menuTree.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
            }
            return menuTree;
        }
    }

    @Override
    public List<SysMenuElement> getUserMenuElemnetList(Integer mId) throws Exception {
        //获取用户的信息
        String[] roleAll = this.getByUserRoles().split(",");
        if (roleAll.length == 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        List<Integer> roles = new ArrayList<>();
        for (String r : roleAll) {
            roles.add(Integer.valueOf(r));
        }
        List<SysMenuElement> elementList = sysMenuElementMapper.getUserMenuElementList(roles, mId);
        if (elementList != null && !elementList.isEmpty()) {
            return elementList;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
    }

    @Override
    public JSONObject getByUser() throws Exception {
        Long memId = ShiroUtils.getUserId();
        int loginType = ShiroUtils.getLoginType();
        JSONObject jsonObject = null;
        if (loginType == 0) {
            jsonObject = JSON.parseObject(JSON.toJSONString(sysUserMapper.selectById(memId)));
        }
        jsonObject.put("loginType", loginType);
        return jsonObject;
    }

    @Override
    @SysLogRecord
    public Integer changePassword(PasswordParam passwordParam) throws Exception {
        if (StringUtils.isAllBlank(passwordParam.getOldPassword(), passwordParam.getPassword(), passwordParam.getConfirmPassword())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Long memId = ShiroUtils.getUserId();
        SysUser user = sysUserMapper.selectById(memId);
        // 校验原有密码
        ByteSource salt = ByteSource.Util.bytes(user.getPasswordSalt());
        String password = new SimpleHash("md5", passwordParam.getOldPassword(), salt, 2).toString();
        if (!password.equals(user.getPassword())) {
            throw new ErrorException(ErrorCode.SYS_LOGIN_UNAUTHORITY);
        }
        // 校验密码是否一致
        if (!passwordParam.getPassword().equals(passwordParam.getConfirmPassword())) {
            throw new ErrorException(ErrorCode.SYS_LOGIN_CONFIRM_PASSWORD_ERROR);
        }
        // 生成新密码
        HashRequest request = new HashRequest
                .Builder()
                .setAlgorithmName("md5")
                .setSource(passwordParam.getPassword())
                .setSalt(ByteSource.Util.bytes(user.getPasswordSalt()))
                .setIterations(2)
                .build();
        String newPassword = defaultHashService.computeHash(request).toHex();
        SysUser newUser = new SysUser();
        newUser.setId(user.getId());
        newUser.setPassword(newPassword);
        return sysUserMapper.updateById(newUser);
    }


    private String getByUserRoles() throws Exception {
        Long memId = ShiroUtils.getUserId();
        int loginType = ShiroUtils.getLoginType();
        if (loginType == 0) {
            return sysUserMapper.selectById(memId).getRoles();
        }
        return null;
    }


}
