/*
 * Copyright (c) 2015 by XuanWu Wireless Technology Co., Ltd. 
 *             All rights reserved                         
 */
package com.lzhpo.core.utils;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @Description 通用JSON处理结果
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2015年4月27日
 * @Version 1.0.0
 */
@Data
public class CommonResp {

	public static final int SUCCESS_STATUS = 0;

	public static final int DEFAULT_FAIL_STATUS = -1;

	/** 0-成功，其它-失败 */
	private int code;
	private Object data;
	private String errorMsg;
	private int count;

	public CommonResp(int status, Object data) {
		this.code = status;
		this.data = data;
		if(data instanceof List){
			count=((List) data).size();
		}
	}

	public CommonResp(){}

	public CommonResp(int status, Object data, String errorMsg) {
		this.code = status;
		this.data = data;
		this.errorMsg = errorMsg;
	}

	public static CommonResp success() {
		return new CommonResp(SUCCESS_STATUS, null);
	}

	public static CommonResp success(Object data) {
		return new CommonResp(SUCCESS_STATUS, data);
	}

	public static CommonResp fail() {
		return fail("系统错误");
	}

	public static CommonResp fail(String errorMsg) {
		return fail(DEFAULT_FAIL_STATUS, errorMsg);
	}

	public static CommonResp fail(int status, String errorMsg) {
		Assert.isTrue(status != SUCCESS_STATUS, "Must be not success status: " + status);
		return new CommonResp(status, null, errorMsg);
	}

	public static CommonResp fail(int status, Object data, String errorMsg) {
		Assert.isTrue(status != SUCCESS_STATUS, "Must be not success status: " + status);
		return new CommonResp(status, data, errorMsg);
	}
}
