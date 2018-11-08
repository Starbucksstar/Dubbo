package com.star.dubbo.core;

import com.alibaba.dubbo.config.ServiceConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceConfigCache {
	private static String DEFAULT_NAME = "_DEFAULT_";
	private static final ConcurrentMap<String, ServiceConfigCache> cacheHolder = new ConcurrentHashMap();
	private static final ServiceConfigCache.KeyGenerator DEFAULAT_KEY_GENERATOR = new ServiceConfigCache.KeyGenerator() {
		@Override
		public String generatorKey(ServiceConfig<?> serviceConfig) {
			String interfaceName = serviceConfig.getInterface();
			interfaceName = interfaceName != null && interfaceName.length() > 0 ? interfaceName : serviceConfig.getInterfaceClass().getName();
			if (interfaceName != null && interfaceName.length() > 0) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(interfaceName).append(serviceConfig.getVersion());
				return stringBuilder.toString();
			} else {
				throw new IllegalArgumentException("No interface info in serviceConfig " + serviceConfig);
			}
		}
	};
	private final String name;
	private final ServiceConfigCache.KeyGenerator keyGenerator;
	private ConcurrentMap<String, ServiceConfig<?>> serviceConfigCache = new ConcurrentHashMap();

	public static ServiceConfigCache getCache() {
		ServiceConfigCache serviceConfigCache = ( ServiceConfigCache)cacheHolder.get(DEFAULT_NAME);
		if (serviceConfigCache != null) {
			return serviceConfigCache;
		} else {
			cacheHolder.putIfAbsent(DEFAULT_NAME, new ServiceConfigCache(DEFAULT_NAME, DEFAULAT_KEY_GENERATOR));
			return (ServiceConfigCache)cacheHolder.get(DEFAULT_NAME);
		}
	}

	private ServiceConfigCache(String name, ServiceConfigCache.KeyGenerator keyGenerator) {
		this.name = name;
		this.keyGenerator = keyGenerator;
	}

	public void export(ServiceConfig<?> serviceConfig) {
		String key = this.keyGenerator.generatorKey(serviceConfig);
		ServiceConfig<?> cacheServiConfig = (ServiceConfig)this.serviceConfigCache.get(key);
		if (cacheServiConfig != null) {
			cacheServiConfig.export();
		} else {
			this.serviceConfigCache.putIfAbsent(this.name, serviceConfig);
			serviceConfig.export();
		}
	}

	private interface KeyGenerator {
		String generatorKey(ServiceConfig<?> var1);
	}
}
