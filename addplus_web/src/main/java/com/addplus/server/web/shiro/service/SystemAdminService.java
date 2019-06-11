package com.addplus.server.web.shiro.service;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.model.authority.ext.SysLoginUser;
import com.addplus.server.api.model.authority.ext.Validate;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.api.utils.RandomValidateCodeUtil;
import com.addplus.server.web.shiro.config.shiro.CustomizedToken;
import com.addplus.server.web.shiro.config.shiro.LoginType;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 类名: SystemServiceImpl
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-26 17:15:40
 * @description 类描述: 系统基本请求实现类
 */
@Service
public class SystemAdminService {

    private Logger logger = LoggerFactory.getLogger(SystemAdminService.class);

    @Autowired
    private RedisTemplate redisTemplateOther;

    @Autowired
    private SysUserMapper sysUserMapper;


    public void adminLogin(SysLoginUser user, Map<String, String> headerMap) throws Exception {
        if (user == null || StringUtils.isBlank(user.getAccount()) && StringUtils.isBlank(user.getPassword())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        } else {
            // 验证码验证
            String verifyToken = headerMap.getOrDefault(StringConstant.ACCESS_CONTROL_ALLOW_AUTHORIZATION, null);
            if (StringUtils.isBlank(user.getVerify()) || StringUtils.isBlank(verifyToken)) {
                throw new ErrorException(ErrorCode.SYS_LOGIN_VERIFY_ERROR);
            }
            if (redisTemplateOther.hasKey(verifyToken)) {
                String checkCode = redisTemplateOther.opsForValue().get(verifyToken).toString();
                redisTemplateOther.delete(verifyToken);
                if (!checkCode.equalsIgnoreCase(user.getVerify())) {
                    throw new ErrorException(ErrorCode.SYS_LOGIN_VERIFY_ERROR);
                }
            } else {
                throw new ErrorException(ErrorCode.SYS_LOGIN_VERIFY_ERROR);
            }
            Subject subject = SecurityUtils.getSubject();
            boolean isLogin = subject.isAuthenticated();
            if (isLogin) {
                subject.logout();
            }
            CustomizedToken token = new CustomizedToken(user.getAccount(), user.getPassword(), LoginType.ADMIN);
            try {
                subject.login(token);
                SysUser newSysUser = (SysUser) subject.getPrincipal();
                user.setLoginAddress(DataUtils.getIpAddress(headerMap));
                // 登录成功后更新用户表里面信息
                boolean isSuccess = updateLoginUser((SysUser) subject.getPrincipal());
                if (!isSuccess) {
                    logger.error(newSysUser.getId() + "管理员更新登录信息不成功");
                }
            } catch (LockedAccountException lae) {
                token.clear();
                throw new ErrorException(ErrorCode.SYS_LOGIN_ACCOUNT_LOCK);
            } catch (AuthenticationException e) {
                token.clear();
                throw new ErrorException(ErrorCode.SYS_LOGIN_CREDENTIAL_ERROR);
            }
            throw new ErrorException(ErrorCode.SYS_SUCCESS, subject.getSession().getId());
        }
    }


    public void userLoginOut() throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Integer loginType = ShiroUtils.getLoginType();
            subject.logout();
            throw new ErrorException(ErrorCode.SYS_SUCCESS, loginType);
        } else {
            throw new ErrorException(ErrorCode.SYS_LOGIN_UNLOGIN);
        }
    }

    public Validate getVerify() throws Exception {
        RandomValidateCodeUtil randomValidateCodeUtil = new RandomValidateCodeUtil();
        Validate validate = randomValidateCodeUtil.getRandomCode();
        String vId = UUID.randomUUID().toString();
        // 设置5分钟超时
        redisTemplateOther.opsForValue().set(vId, validate.getCode(), 5, TimeUnit.MINUTES);
        validate.setvToken(vId);
        validate.setCode(null);
        return validate;
    }

    private Boolean updateLoginUser(SysUser sysUser) {
        SysUser sysUserUpdate = new SysUser();
        sysUserUpdate.setId(sysUser.getId());
        sysUserUpdate.setLoginAddress(sysUser.getLoginAddress());
        sysUserUpdate.setLoginCount(sysUser.getLoginCount() + 1);
        sysUserUpdate.setLoginTime(new Date());
        sysUserUpdate.setGmtModified(new Date());
        sysUserUpdate.setModifyUser(ShiroUtils.getUserAccount());
        int count = sysUserMapper.updateById(sysUserUpdate);
        if (count > 0) {
            return true;
        }
        return false;
    }
}