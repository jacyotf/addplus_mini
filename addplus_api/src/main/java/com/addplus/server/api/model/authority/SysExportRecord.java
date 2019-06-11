package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author fuyq
 * @date 2019/3/5
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysExportRecord extends BaseModel {

    /**
     * 关联用户Id
     */
    private Long userId;

    /**
     * 导出数量
     */
    private Long exportNum;

    /**
     * 导出状态0:正在处理，1:处理成功，2:处理失败
     */
    private Integer exportStatus;

    /**
     * 导出来源
     */
    private String exportFrom;

    /**
     * 导出下载地址
     */
    private String exportUrl;

    /**
     * 处理完成时间
     */
    private Date completionTime;
}
