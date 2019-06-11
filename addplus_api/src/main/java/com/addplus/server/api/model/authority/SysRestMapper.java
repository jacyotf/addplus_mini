package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统接口映射表
 * 
 * @author zhangjiehang
 * @date 2019-02-22 00:06:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysRestMapper extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 接口名称
	 */
	private String restName;

	/**
	 * 接口请求链接
	 */
	private String restUrl;

}
