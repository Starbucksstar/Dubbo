package com.star.dubbo.core;

import com.star.dubbo.util.PropertiesUtil;

public class DubboPropertiesConfig
{
	public static final String DUBBO_APPLICATION_NAME = PropertiesUtil.getConfigValue("dubbo.application.name");
	public static final String DUBBO_APPLICATION_OWNER = "crt";
	public static final String DUBBO_REGISTRY_ADDRESS = PropertiesUtil.getConfigValue("dubbo.registry.address");
	public static final String DUBBO_REGISTRY_PROTOCOL = PropertiesUtil.getValue("dubbo.registry.protocol");
	public static final Boolean DUBBO_REGISTRY_DYNAMIC = Boolean.valueOf(PropertiesUtil.getValue("dubbo.registry.dynamic"));
	public static final String DUBBO_SERVICE_VERSION = PropertiesUtil.getValue("dubbo.service.version");
	public static final String DUBBO_PROTOCOL_PROTOCOL = PropertiesUtil.getValue("dubbo.protocol.protocol");
	public static final String DUBBO_PROTOCOL_SERIALIZATION = PropertiesUtil.getValue("dubbo.protocol.serialization");
	public static final Integer DUBBO_PROTOCOL_PORT = Integer.valueOf(PropertiesUtil.getConfigValue("dubbo.protocol.port"));
	public static final Integer HESSIAN_PROTOCOL_PORT = Integer.valueOf(PropertiesUtil.getConfigValue("hessian.protocol.port"));
	public static final String DUBBO_PROTOCOL_DISPATCHER = PropertiesUtil.getValue("dubbo.protocol.dispatcher");
	public static final String DUBBO_PROTOCOL_THREADPOOL = PropertiesUtil.getValue("dubbo.protocol.threadpool");
	public static final Integer DUBBO_PROTOCOL_THREADS = Integer.valueOf(PropertiesUtil.getConfigValue("dubbo.protocol.threads"));
	public static final Integer DUBBO_TIMEOUT = Integer.valueOf(PropertiesUtil.getConfigValue("dubbo.timeout"));
	public static final Integer DUBBO_CONSUMER_TIMEOUT = Integer.valueOf(PropertiesUtil.getConfigValue("dubbo.consumer.timeout"));
	public static final Boolean DUBBO_CHECK = Boolean.valueOf(PropertiesUtil.getValue("dubbo.check"));
	public static final Boolean DUBBO_MONITOR = Boolean.valueOf(PropertiesUtil.getConfigValue("dubbo.monitor"));
	public static final Integer DUBBO_EXECUTES = Integer.valueOf(PropertiesUtil.getConfigValue("dubbo.protocol.threads"));
	public static final String DUBBO_CLUSTER = PropertiesUtil.getValue("dubbo.cluster");
	public static final Integer DUBBO_RETRIES = Integer.valueOf(PropertiesUtil.getValue("dubbo.retries"));
	public static final String DUBBO_LOADBALANCE = PropertiesUtil.getValue("dubbo.loadbalance");

	public DubboPropertiesConfig() {
	}

	public static String getRegistry(String key) {
		return PropertiesUtil.getConfigValue(key);
	}
}
