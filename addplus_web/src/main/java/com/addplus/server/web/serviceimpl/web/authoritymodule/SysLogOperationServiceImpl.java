package com.addplus.server.web.serviceimpl.web.authoritymodule;

import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysLogOperationMapper;
import com.addplus.server.api.model.authority.SysLogOperation;
import com.addplus.server.api.model.authority.SysLogOperationNormal;
import com.addplus.server.api.mongodao.SysLogOperationDao;
import com.addplus.server.api.service.web.authoritymodule.SysLogOperationService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.api.utils.JsonFormatTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysLogOperationServiceImpl implements SysLogOperationService {

    @Autowired(required = false)
    private SysLogOperationDao sysLogOperationDao;

    @Autowired
    private SysLogOperationMapper sysLogOperationMapper;

    @Value("${log.record.storage}")
    private Integer storageType;

    @Override
    public Page getListByPage(String begin, String end, Integer logType, String methodName, String module, Integer pageNo, Integer pageSize, Boolean sort, String sortKey) throws ErrorException {
        if (DataUtils.EmptyOrNegativeOrZero(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page page = null;
        if (storageType == 1) {
            page = new Page(pageNo, pageSize);
            QueryWrapper<SysLogOperationNormal> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(sortKey)) {
                queryWrapper.orderBy(sort, sort, sortKey);
            }else {
                queryWrapper.orderByDesc("gmt_create");
            }
            if (null != logType && logType != -1) {
                queryWrapper.eq("log_type", logType);
            }
            if (StringUtils.isNoneBlank(begin, end)) {
                queryWrapper.ge("gmt_create", new Date(Long.valueOf(begin)));
                queryWrapper.le("gmt_create", new Date(Long.valueOf(end)));
            }
            if (StringUtils.isNotBlank(methodName)) {
                queryWrapper.eq("method_name", methodName);
            }
            if (StringUtils.isNotBlank(module)) {
                queryWrapper.eq("module", module);
            }
            queryWrapper.select("id", "log_type","module","method","method_name","modify_user","service","method","gmt_create");
            page = (Page) sysLogOperationMapper.selectPage(page, queryWrapper);
            if (page == null || page.getRecords() == null || page.getRecords().isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
            }
        } else if (storageType == 2) {
            Query<SysLogOperation> sysLogOperations = sysLogOperationDao.createQuery(SysLogOperation.class).disableValidation();
            sysLogOperations.order(Sort.descending("gmtCreate"));
            if (StringUtils.isNotBlank(sortKey)) {
                sysLogOperations.order(sort ? Sort.ascending(sortKey) : Sort.descending(sortKey));
            }else {
                sysLogOperations.order(Sort.descending("gmtCreate"));
            }
            if (null != logType && logType != -1) {
                sysLogOperations.field("logType").equal(logType);
            }
            if (StringUtils.isNoneBlank(begin, end)) {
                sysLogOperations.field("gmtCreate").greaterThanOrEq(new Date(Long.valueOf(begin)));
                sysLogOperations.field("gmtCreate").lessThanOrEq(new Date(Long.valueOf(end)));
            }
            if (StringUtils.isNotBlank(methodName)) {
                sysLogOperations.field("methodName").contains(methodName);
            }
            sysLogOperations.project("result", false);
            sysLogOperations.project("param", false);
            FindOptions limit = new FindOptions();
            limit.skip((pageNo - 1) * pageSize).limit(pageSize);
            // 总页数
            long count = sysLogOperationDao.count(sysLogOperations);
            List<SysLogOperation> list = sysLogOperations.asList(limit);
            if (null == list) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
            }
            page = new Page<SysLogOperation>(pageNo, pageSize).setRecords(list).setTotal(count).setCurrent(pageNo).setSize(pageSize);
        }

        return page;
    }

    @Override
    @SysLogRecord
    public Boolean deleteAllSysLogRecord() throws ErrorException {
        if (storageType == 1) {
            QueryWrapper<SysLogOperationNormal> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_deleted", 0);
            sysLogOperationMapper.delete(queryWrapper);
        } else {
            Query<SysLogOperation> query = sysLogOperationDao.createQuery(SysLogOperation.class);
            sysLogOperationDao.deleteByQuery(query);
        }
        return true;
    }

    @Override
    public SysLogOperation getByPrimary(String id) throws ErrorException {
        if (DataUtils.isEmpty(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysLogOperation sysLogOperation = null;
        if (storageType == 1) {
            SysLogOperationNormal sysLogOperationNormal = sysLogOperationMapper.selectById(id);
            sysLogOperationNormal.setResult(JsonFormatTool.formatJson(sysLogOperationNormal.getResult()));
            sysLogOperationNormal.setParam(JsonFormatTool.formatJson(sysLogOperationNormal.getParam()));
            if (sysLogOperationNormal != null) {
                sysLogOperation = new SysLogOperation();
                BeanUtils.copyProperties(sysLogOperationNormal, sysLogOperation);
            }
        } else {
            sysLogOperation = sysLogOperationDao.get(new ObjectId(id));
            sysLogOperation.setResult(JsonFormatTool.formatJson(sysLogOperation.getResult()));
            sysLogOperation.setParam(JsonFormatTool.formatJson(sysLogOperation.getParam()));
        }

        return sysLogOperation;
    }

}
