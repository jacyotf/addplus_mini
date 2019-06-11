package com.addplus.server.connector.mysql.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author fuyq
 * @date 2019/3/3
 */
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object gmtCreate = this.getFieldValByName("gmtCreate", metaObject);
        Object gmtModified = this.getFieldValByName("gmtModified", metaObject);
        Object isDeleted = this.getFieldValByName("isDeleted", metaObject);
        if (gmtCreate == null) {
            this.setInsertFieldValByName("gmtCreate", new Date(), metaObject);
        }
        if (gmtModified == null) {
            this.setInsertFieldValByName("gmtModified", new Date(), metaObject);
        }
        if (isDeleted == null) {
            this.setInsertFieldValByName("isDeleted", 0, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object gmtModified = this.getFieldValByName("gmtModified", metaObject);
        if (gmtModified == null) {
            this.setUpdateFieldValByName("gmtModified", new Date(), metaObject);
        }
    }
}
