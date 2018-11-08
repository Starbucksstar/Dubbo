package com.star.dubbo.core;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.star.dubbo.util.AddressUtil;

import java.util.HashMap;
import java.util.Map;

public class DubboConfig {
	private static DubboConfig dubboConfig = null;
	private ApplicationConfig application = null;
	private RegistryConfig registry = null;
	private Map<String, RegistryConfig> registryConfigMap = new HashMap();
	private MonitorConfig monitConfig = null;

	private DubboConfig() {
		this.application = new ApplicationConfig();
		this.application.setName(DubboPropertiesConfig.DUBBO_APPLICATION_NAME);
		this.application.setOwner("star");
		this.registry = new RegistryConfig();
		String fileDir = "dubbo/dubbo-registry-" + AddressUtil.getLocalIP() + "-" + DubboPropertiesConfig.DUBBO_PROTOCOL_PORT + ".dubbo.cache";
		this.registry.setFile(fileDir);
		String timeout = System.getProperty("dubbo.registry.timeout");
		if (null != timeout) {
			this.registry.setTimeout(Integer.parseInt(timeout));
		}

		this.registry.setAddress(DubboPropertiesConfig.DUBBO_REGISTRY_ADDRESS);
		this.registry.setProtocol(DubboPropertiesConfig.DUBBO_REGISTRY_PROTOCOL);
		this.registry.setDynamic(DubboPropertiesConfig.DUBBO_REGISTRY_DYNAMIC);
		this.monitConfig = new MonitorConfig();
		this.monitConfig.setProtocol("registry");
	}

	public static DubboConfig getInstance() {
		if (null == dubboConfig) {
			dubboConfig = new DubboConfig();
		}

		return dubboConfig;
	}

	public ApplicationConfig getApplication() {
		return this.application;
	}

	public RegistryConfig getRegistry() {
		return this.registry;
	}

	public RegistryConfig getRegistry(String key) {
		RegistryConfig registryConfig = (RegistryConfig)this.registryConfigMap.get(key);
		if (null == registryConfig) {
			String registryAddress = DubboPropertiesConfig.getRegistry(key);
			if (null == registryAddress) {
				return null;
			}

			RegistryConfig registry = new RegistryConfig();
			String fileDir = "dubbo/dubbo-registry-" + key + "-" + DubboPropertiesConfig.DUBBO_PROTOCOL_PORT + ".dubbo.cache";
			registry.setFile(fileDir);
			registry.setAddress(registryAddress);
			registry.setProtocol(DubboPropertiesConfig.DUBBO_REGISTRY_PROTOCOL);
			registry.setDynamic(DubboPropertiesConfig.DUBBO_REGISTRY_DYNAMIC);
			String timeout = System.getProperty("dubbo.registry.timeout");
			if (null != timeout) {
				registry.setTimeout(Integer.parseInt(timeout));
			}

			this.registryConfigMap.put(key, registry);
		}

		return (RegistryConfig)this.registryConfigMap.get(key);
	}

	public MonitorConfig getMonitConfig() {
		return this.monitConfig;
	}

	public void setMonitConfig(MonitorConfig monitConfig) {
		this.monitConfig = monitConfig;
	}
}