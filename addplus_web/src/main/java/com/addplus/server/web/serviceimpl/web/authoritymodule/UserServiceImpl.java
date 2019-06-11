package com.addplus.server.web.serviceimpl.web.authoritymodule;

import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.service.web.authoritymodule.UserService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DefaultHashService defaultHashService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public SysUser selectByUsername(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        SysUser sysUser = new SysUser();
        sysUser.setAccount(account);
        sysUser.setIsDeleted(0);
        SysUser sysUserOne = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        return sysUserOne;
    }

    @Override
    @SysLogRecord
    public Boolean updateLoginUser(SysUser sysUser) {
        SysUser sysUserUpdate = new SysUser();
        sysUserUpdate.setId(sysUser.getId());
        sysUserUpdate.setLoginAddress(sysUser.getLoginAddress());
        sysUserUpdate.setLoginCount(sysUser.getLoginCount() + 1);
        sysUserUpdate.setLoginTime(new Date());
        sysUserUpdate.setGmtModified(new Date());
        String modifyUser = ShiroUtils.getUserAccount();
        sysUserUpdate.setModifyUser(modifyUser);
        int count = sysUserMapper.updateById(sysUserUpdate);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    @SysLogRecord
    public Boolean addUser(SysUser sysUser) throws ErrorException {
        if (sysUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        } else {
            if (StringUtils.isBlank(sysUser.getRoles()) || StringUtils.isBlank(sysUser.getAccount()) || StringUtils.isBlank(sysUser.getPassword()) || StringUtils.isBlank(sysUser.getNickname()) || StringUtils.isBlank(sysUser.getPhone())) {
                throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
            }
        }
        sysUser.setGmtCreate(new Date());
        sysUser.setLoginCount(0);
        sysUser.setIsDeleted(0);
        sysUser.setStatus(0);
        String modifyUser = ShiroUtils.getUserAccount();
        ObjectId objectId = new ObjectId();
        sysUser.setPasswordSalt(objectId.toString());
        sysUser.setModifyUser(modifyUser);
        //生成加密后的密码
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("md5").setSource(sysUser.getPassword())
                .setSalt(ByteSource.Util.bytes(sysUser.getPasswordSalt())).setIterations(2).build();
        String hexPassword = defaultHashService.computeHash(request).toHex();
        sysUser.setPassword(hexPassword);
        int count = sysUserMapper.insert(sysUser);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SysLogRecord
    public Boolean deleteUserById(Long id) throws Exception {
        if (id == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        } else if (id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        String modifyUser = ShiroUtils.getUserAccount();
        sysUser.setModifyUser(modifyUser);
        sysUserMapper.updateById(sysUser);
        int count = sysUserMapper.deleteById(id);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SysUser selectUserById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setIsDeleted(0);
        SysUser sysUserNew = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        if (sysUserNew == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        } else {
            if (sysUserNew.getStatus() == 1) {
                //抛出冻结异常码
                throw new ErrorException(ErrorCode.SYS_LOGIN_MEMBER_DISABLE);
            }
        }
        return sysUserNew;
    }

    @Override
    @SysLogRecord
    public SysUser modifyUserGetInfoById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setIsDeleted(0);
        SysUser sysUserNew = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        if (sysUserNew == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysUserNew;
    }

    @Override
    public String encryptString(SysUser sysUser) throws Exception {
        if (sysUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        //生成加密后的密码
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("md5").setSource(sysUser.getPassword())
                .setSalt(ByteSource.Util.bytes(sysUser.getPasswordSalt())).setIterations(2).build();
        String hexPassword = defaultHashService.computeHash(request).toHex();
        return hexPassword;
    }

    @Override
    @SysLogRecord
    public Boolean updateUser(SysUser sysUser) throws Exception {
        if (sysUser == null || sysUser.getId() == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysUser.setGmtModified(new Date());
        String modifyUser = ShiroUtils.getUserAccount();
        sysUser.setModifyUser(modifyUser);
        if (StringUtils.isNotBlank(sysUser.getPassword())) {
            if (StringUtils.isBlank(sysUser.getAccount())) {
                throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
            }
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", sysUser.getId());
            queryWrapper.select("id", "password_salt");
            SysUser sysUserOld = sysUserMapper.selectOne(queryWrapper);
            sysUser.setPasswordSalt(sysUserOld.getPasswordSalt());
            sysUser.setPassword(encryptString(sysUser));
        }
        int count = sysUserMapper.updateById(sysUser);
        return count > 0;
    }

    @Override
    public IPage<SysUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception {
        if (DataUtils.EmptyOrNegative(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        IPage<SysUser> sysUserIPage = sysUserMapper.selectPage(page, queryWrapper);
        if (sysUserIPage == null || sysUserIPage.getRecords() == null || sysUserIPage.getRecords().isEmpty()) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysUserIPage;
    }

    @Override
    public SysUser getByUser() throws Exception {
        Long memId = ShiroUtils.getUserId();
        SysUser sysUser = new SysUser();
        sysUser.setIsDeleted(0);
        sysUser.setId(memId);
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));

    }

    @Override
    public Integer getUserNameCount(String account) throws Exception {
        if (StringUtils.isBlank(account)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        int res = sysUserMapper.getSysCountNum(account);
        return res;
    }



}
