package com.star.dubbo.core;

import com.alibaba.dubbo.rpc.service.GenericService;

import java.util.Map;

public class DubboGenericService {
	private GenericService genericService;

	public DubboGenericService(GenericService genericService) {
		this.genericService = genericService;
	}

	public Object invoke(String methodName, String parameterClassName, Map<String, Object> parameterMap) {
		return this.genericService.$invoke(methodName, new String[]{parameterClassName}, new Object[]{parameterMap});
	}
}