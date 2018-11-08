
package com.star.dubbo.core;

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DubboInvoker {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private DubboConfig dubboConfig = DubboConfig.getInstance();
	private ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();
	private ServiceConfigCache serviceConfigCache = ServiceConfigCache.getCache();

	public DubboInvoker() {
	}

	public void registryService(Class<?> clazz, Object ref, String version, int timeout, boolean netty, boolean jetty, boolean validation) {
		this.log.info("registry dubboService====class====" + clazz + "====ref:" + ref);
		ServiceConfig<Object> serviceConfig = new ServiceConfig();
		serviceConfig.setApplication(this.dubboConfig.getApplication());
		serviceConfig.setRegistry(this.dubboConfig.getRegistry());
		serviceConfig.setInterface(clazz);
		serviceConfig.setRef(ref);
		serviceConfig.setVersion(version);
		if (timeout > 10) {
			serviceConfig.setTimeout(timeout);
		} else {
			serviceConfig.setTimeout(DubboPropertiesConfig.DUBBO_TIMEOUT);
		}

		serviceConfig.setExecutes(DubboPropertiesConfig.DUBBO_EXECUTES);
		serviceConfig.setLoadbalance(DubboPropertiesConfig.DUBBO_LOADBALANCE);
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setName(DubboPropertiesConfig.DUBBO_PROTOCOL_PROTOCOL);
		protocol.setSerialization(DubboPropertiesConfig.DUBBO_PROTOCOL_SERIALIZATION);
		protocol.setPort(DubboPropertiesConfig.DUBBO_PROTOCOL_PORT);
		protocol.setDispatcher(DubboPropertiesConfig.DUBBO_PROTOCOL_DISPATCHER);
		protocol.setThreadpool(DubboPropertiesConfig.DUBBO_PROTOCOL_THREADPOOL);
		protocol.setThreads(DubboPropertiesConfig.DUBBO_PROTOCOL_THREADS);
		ProtocolConfig hessianProtocol = new ProtocolConfig();
		hessianProtocol.setServer("jetty");
		hessianProtocol.setName("hessian");
		hessianProtocol.setSerialization(DubboPropertiesConfig.DUBBO_PROTOCOL_SERIALIZATION);
		hessianProtocol.setPort(DubboPropertiesConfig.HESSIAN_PROTOCOL_PORT);
		hessianProtocol.setDispatcher(DubboPropertiesConfig.DUBBO_PROTOCOL_DISPATCHER);
		hessianProtocol.setThreadpool(DubboPropertiesConfig.DUBBO_PROTOCOL_THREADPOOL);
		hessianProtocol.setThreads(DubboPropertiesConfig.DUBBO_PROTOCOL_THREADS);
		List<ProtocolConfig> protocolList = new ArrayList();
		if (netty) {
			protocolList.add(protocol);
		}

		if (jetty) {
			this.log.info("hessian jetty port:" + DubboPropertiesConfig.HESSIAN_PROTOCOL_PORT);
			protocolList.add(hessianProtocol);
		}

		serviceConfig.setProtocols(protocolList);
		serviceConfig.setValidation(validation ? "true" : "false");
		if (DubboPropertiesConfig.DUBBO_MONITOR) {
			serviceConfig.setMonitor(this.dubboConfig.getMonitConfig());
		}

		this.cacheProxy(clazz);
		this.serviceConfigCache.export(serviceConfig);
	}

	private void cacheProxy(Class clazz) {
	}

	public <T> T getDubboService(Class<T> t, String version, int timeout, boolean retries, String registryAddress, boolean validation) {
		this.log.info("consumer dubboService====class====" + t);
		ReferenceConfig<T> referenceConfig = new ReferenceConfig();
		referenceConfig.setApplication(this.dubboConfig.getApplication());
		if (null != registryAddress && registryAddress.length() > 0) {
			RegistryConfig rgsConfig = this.dubboConfig.getRegistry(registryAddress);
			if (null == rgsConfig) {
				return null;
			}

			this.log.info("fileDir:" + rgsConfig.getFile());
			referenceConfig.setRegistry(rgsConfig);
		} else {
			referenceConfig.setRegistry(this.dubboConfig.getRegistry());
		}

		referenceConfig.setInterface(t);
		if (timeout <= 0) {
			timeout = DubboPropertiesConfig.DUBBO_CONSUMER_TIMEOUT;
		}

		referenceConfig.setTimeout(timeout);
		ConsumerConfig consumer = new ConsumerConfig();
		consumer.setTimeout(timeout);
		referenceConfig.setConsumer(consumer);
		if (!retries) {
			referenceConfig.setRetries(0);
		} else {
			referenceConfig.setRetries(DubboPropertiesConfig.DUBBO_RETRIES);
		}

		referenceConfig.setInjvm(false);
		referenceConfig.setVersion(version);
		referenceConfig.setProtocol("dubbo");
		referenceConfig.setCheck(DubboPropertiesConfig.DUBBO_CHECK);
		referenceConfig.setInit(true);
		referenceConfig.setLazy(false);
		referenceConfig.setValidation(validation ? "true" : "false");
		if (DubboPropertiesConfig.DUBBO_MONITOR) {
			referenceConfig.setMonitor(this.dubboConfig.getMonitConfig());
		}

		return this.referenceConfigCache.get(referenceConfig);
	}

	public GenericService getGenericService(String interfaceName, String version, int timeout, boolean retries, String registryAddress, boolean validation) {
		ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig();
		referenceConfig.setApplication(this.dubboConfig.getApplication());
		if (null != registryAddress && registryAddress.length() > 0) {
			RegistryConfig rgsConfig = this.dubboConfig.getRegistry(registryAddress);
			if (null == rgsConfig) {
				return null;
			}

			referenceConfig.setRegistry(rgsConfig);
		} else {
			referenceConfig.setRegistry(this.dubboConfig.getRegistry());
		}

		if (timeout > 99) {
			referenceConfig.setTimeout(timeout);
			ConsumerConfig consumer = new ConsumerConfig();
			consumer.setTimeout(timeout);
			referenceConfig.setConsumer(consumer);
		}

		if (!retries) {
			referenceConfig.setRetries(0);
		} else {
			referenceConfig.setRetries(DubboPropertiesConfig.DUBBO_RETRIES);
		}

		referenceConfig.setInterface(interfaceName);
		referenceConfig.setInjvm(false);
		referenceConfig.setVersion(version);
		referenceConfig.setGeneric(true);
		referenceConfig.setCheck(DubboPropertiesConfig.DUBBO_CHECK);
		referenceConfig.setValidation(validation ? "true" : "false");
		if (DubboPropertiesConfig.DUBBO_MONITOR) {
			referenceConfig.setMonitor(this.dubboConfig.getMonitConfig());
		}

		return (GenericService)this.referenceConfigCache.get(referenceConfig);
	}
}
