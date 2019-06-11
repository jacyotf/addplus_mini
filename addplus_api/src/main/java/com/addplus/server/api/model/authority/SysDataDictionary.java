package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 数据字典基础表
 * 
 * @author zhangjiehang
 * @date 2019-02-22 00:06:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDataDictionary extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * key名称
	 */
	private String redisKey;

	/**
	 * 内容值(json格式)
	 */
	private String redisValue;

}
