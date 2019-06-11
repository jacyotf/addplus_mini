package com.addplus.server.web.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysExportRecordMapper;
import com.addplus.server.api.model.authority.SysExportRecord;
import com.addplus.server.api.service.web.authoritymodule.SysExportRecordService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.web.shiro.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysExportRecordServiceImpl extends ServiceImpl<SysExportRecordMapper, SysExportRecord> implements SysExportRecordService {

    @Override
    public IPage<SysExportRecord> getListByPage(Integer pageNo, Integer pageSize) throws Exception {
        if (DataUtils.EmptyOrNegative(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysExportRecord> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SysExportRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", ShiroUtils.getUserId());
        queryWrapper.orderByDesc("gmt_create");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public SysExportRecord getExportRecordById(Long id) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        return baseMapper.selectById(id);
    }

    @Override
    public Integer insertExportRecord(SysExportRecord sysExportRecord) throws Exception {
        Long userId = ShiroUtils.getUserId();
        sysExportRecord.setUserId(userId);
        sysExportRecord.setModifyUser(ShiroUtils.getUserAccount());
        return baseMapper.insert(sysExportRecord);
    }

    @Override
    public Integer updateExportRecord(SysExportRecord sysExportRecord) throws Exception {
        if (sysExportRecord == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        if (DataUtils.EmptyOrNegativeOrZero(sysExportRecord.getId())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        return baseMapper.updateById(sysExportRecord);
    }
}
