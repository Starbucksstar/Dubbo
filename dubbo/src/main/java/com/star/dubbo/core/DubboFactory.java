package com.star.dubbo.core;

import com.alibaba.dubbo.rpc.service.GenericService;


public class DubboFactory
{
	private static DubboInvoker dubboInvoker = new DubboInvoker();

	public DubboFactory() {
	}

	public static <T> T getDubboService(Class<T> T) {
		return dubboInvoker.getDubboService(T, "1.0.0", 0, false, (String)null, false);
	}

	public static <T> T getDubboService(Class<T> T, boolean retries) {
		return dubboInvoker.getDubboService(T, "1.0.0", 0, retries, (String)null, false);
	}

	public static <T> T getDubboService(Class<T> T, String registryAddress) {
		return dubboInvoker.getDubboService(T, "1.0.0", 0, true, registryAddress, false);
	}

	public static <T> T getDubboService(Class<T> T, String version, String registryAddress) {
		return dubboInvoker.getDubboService(T, version, 0, true, registryAddress, false);
	}

	public static <T> T getDubboService(Class<T> T, String version, int timeout, boolean retries, String registryAddress, boolean validation) {
		return dubboInvoker.getDubboService(T, version, timeout, retries, registryAddress, validation);
	}

	public static void registryService(Class<?> clazz, Object ref) {
		dubboInvoker.registryService(clazz, ref, "1.0.0", 0, true, false, false);
	}

	public static void registryService(Class<?> clazz, Object ref, int timeout) {
		dubboInvoker.registryService(clazz, ref, "1.0.0", timeout, true, false, false);
	}

	public static void registryService(Class<?> clazz, Object ref, String version) {
		dubboInvoker.registryService(clazz, ref, version, 0, true, false, false);
	}

	public static void registryService(Class<?> clazz, Object ref, String version, int timeout, boolean netty, boolean jetty, boolean validation) {
		dubboInvoker.registryService(clazz, ref, version, timeout, netty, jetty, validation);
	}

	public static DubboGenericService getDubboGennericService(String interfaceName, String registry) {
		GenericService genericService = dubboInvoker.getGenericService(interfaceName, "1.0.0", 0, false, registry, false);
		DubboGenericService dubboGenericService = new DubboGenericService(genericService);
		return dubboGenericService;
	}

	public static DubboGenericService getDubboGennericService(String interfaceName) {
		GenericService genericService = dubboInvoker.getGenericService(interfaceName, "1.0.0", 0, false, (String)null, false);
		DubboGenericService dubboGenericService = new DubboGenericService(genericService);
		return dubboGenericService;
	}

	public static DubboGenericService getDubboGennericService(String interfaceName, String version, String registry) {
		GenericService genericService = dubboInvoker.getGenericService(interfaceName, version, 0, false, registry, false);
		DubboGenericService dubboGenericService = new DubboGenericService(genericService);
		return dubboGenericService;
	}
}
