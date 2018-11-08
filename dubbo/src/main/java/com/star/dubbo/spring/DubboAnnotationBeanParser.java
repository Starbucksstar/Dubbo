package com.star.dubbo.spring;


import com.star.dubbo.annotation.DubboConsumer;
import com.star.dubbo.annotation.DubboService;
import com.star.dubbo.core.DubboFactory;
import com.star.dubbo.util.BeanUtil;
import com.star.dubbo.util.PropertiesUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Data
public class DubboAnnotationBeanParser implements ApplicationContextAware, EmbeddedValueResolverAware
{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext applicationContext;
	private static StringValueResolver strValResol;
	private String packagePath = "com.star";
	private Properties conf;

	public DubboAnnotationBeanParser() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.conf != null) {
			PropertiesUtil.setConf(this.conf);
		}

		this.applicationContext = applicationContext;
		this.log.info(System.getProperty("user.dir"));
		this.log.info("applicationContext:" + applicationContext.getApplicationName() + ";obj:" + applicationContext.toString());

		try {
			String[] paths = this.packagePath.split(",");
			List<Class> classList = new ArrayList();
			String[] var4 = paths;
			int var5 = paths.length;

			for(int var6 = 0; var6 < var5; ++var6) {
				String path = var4[var6];
				classList.addAll(BeanUtil.getClasses(path));
			}

			this.log.info("clssList.size():" + classList.size());
			Iterator var10 = classList.iterator();

			while(var10.hasNext()) {
				Class clazz = (Class)var10.next();
				this.publishDubboService(clazz);
				this.initDubboConsumer(clazz);
			}

		} catch (Exception var8) {
			this.log.error("dubbo service init exception {}", var8.getMessage(), var8);
			BootstrapException exception = new BootstrapException(var8.getMessage());
			throw exception;
		}
	}

	private void publishDubboService(Class<?> clazz) {
		Annotation[] annotations = clazz.getAnnotations();
		if (null != annotations && annotations.length > 0) {
			Annotation[] var3 = annotations;
			int var4 = annotations.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				Annotation annotation = var3[var5];
				if (annotation instanceof DubboService) {
					Class<?>[] clazzInterfaces = clazz.getInterfaces();
					this.log.info("className:" + clazz.toString());
					Object obj = this.applicationContext.getBean(clazz);
					DubboService dubboService = (DubboService)annotation;
					String version = dubboService.version();
					int timeout = dubboService.timeout();
					String timeoutExp = dubboService.timeoutExp();

					try {
						if (timeoutExp.indexOf("${") > -1 && timeoutExp.lastIndexOf("}") > 0) {
							timeoutExp = strValResol.resolveStringValue(timeoutExp);
							timeout = Integer.parseInt(timeoutExp);
							this.log.info(clazz + ":" + dubboService.timeoutExp() + ":" + timeoutExp);
						}
					} catch (Exception var20) {
						timeout = 0;
					}

					boolean netty = dubboService.netty();
					boolean jetty = dubboService.jetty();
					boolean validation = dubboService.validation();
					if (null != obj) {
						Class[] var16 = clazzInterfaces;
						int var17 = clazzInterfaces.length;

						for(int var18 = 0; var18 < var17; ++var18) {
							Class cls = var16[var18];
							DubboFactory.registryService(cls, obj, version, timeout, netty, jetty, validation);
						}
					}
					break;
				}
			}

		}
	}

	private void initDubboConsumer(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = clazz.getDeclaredFields();
		Field[] var3 = fields;
		int var4 = fields.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Field field = var3[var5];
			Annotation[] fieldAnnotations = field.getAnnotations();
			Annotation[] var8 = fieldAnnotations;
			int var9 = fieldAnnotations.length;

			for(int var10 = 0; var10 < var9; ++var10) {
				Annotation fieldAnnotation = var8[var10];
				if (fieldAnnotation instanceof DubboConsumer) {
					Object obj = this.applicationContext.getBean(clazz);
					if (null != obj) {
						DubboConsumer consumer = (DubboConsumer)fieldAnnotation;
						String version = consumer.version();
						String timeoutExp = consumer.timeoutExp();
						int timeout = consumer.timeout();

						try {
							if (timeoutExp.indexOf("${") > -1 && timeoutExp.lastIndexOf("}") > 0) {
								timeoutExp = strValResol.resolveStringValue(timeoutExp);
								timeout = Integer.parseInt(timeoutExp);
								this.log.info(clazz + ":" + consumer.timeoutExp() + ":" + timeoutExp);
							}
						} catch (Exception var23) {
							timeout = 0;
						}

						boolean retries = consumer.retries();
						String registryAddress = consumer.registry();
						boolean validation = consumer.validation();
						Object dubboConsumer = DubboFactory.getDubboService(field.getType(), version, timeout, retries, registryAddress, validation);
						field.setAccessible(true);
						field.set(obj, dubboConsumer);

						try {
							Object targetBean = AopProxyUtils.getSingletonTarget(obj);
							if (null != targetBean) {
								this.log.info("springTargetBean:" + targetBean);
								ReflectionUtils.setField(field, targetBean, dubboConsumer);
							}
						} catch (Exception var22) {
							this.log.info("int springTargetBean failed:" + obj);
						}
					}
				}
			}
		}

	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver stringValueResolver)
	{

	}
}

