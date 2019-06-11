package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 程序协议通用表
 * 
 * @author zhangjiehang
 * @date 2019-02-22 00:06:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysCommonProtocol extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 协议key
	 */
	private String protocolKey;

	/**
	 * 协议名称
	 */
	private String protocolName;

	/**
	 * 协议位置
	 */
	private String protocolLocation;

	/**
	 * 协议内容
	 */
	private String protocolContext;

}
