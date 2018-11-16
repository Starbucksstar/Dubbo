package com.star.dubbo.spring;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.star.dubbo.annotation.DubboConsumer;
import com.star.dubbo.annotation.DubboService;
import com.star.dubbo.core.DubboFactory;
import com.star.dubbo.util.BeanUtil;
import com.star.dubbo.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

@Configuration
@Lazy(false)
public class DubboAnnotationBeanParser implements ApplicationContextAware
{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Reference
	private ApplicationContext applicationContext;
	private String packagePath = "com.star";
	private Properties conf;

	public DubboAnnotationBeanParser(){
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		if (this.conf != null)
		{
			PropertiesUtil.setConf(this.conf);
		}

		this.applicationContext = applicationContext;
		this.log.info(System.getProperty("user.dir"));
		this.log.info("applicationContext:" + applicationContext.getApplicationName() + ";obj:" + applicationContext.toString());

		try
		{
			List<Class> classList = BeanUtil.getClasses(this.packagePath);
			this.log.info("clssList.size():" + classList.size());
			for(Class clazz:classList)
			{
				this.publishDubboService(clazz);
				this.initDubboConsumer(clazz);
			}
		}
		catch (Exception e)
		{
			this.log.error("dubbo service init exception {}", e.getMessage(), e);
			BootstrapException exception = new BootstrapException(e.getMessage());
			throw exception;
		}

	}


	private void publishDubboService(Class<?> clazz)
	{
		Annotation[] annotations = clazz.getAnnotations();
		if (null != annotations && annotations.length > 0)
		{
			for (Annotation annotation : annotations)
			{
				if (annotation instanceof DubboService)
				{
					Class<?>[] clazzInterfaces = clazz.getInterfaces();
					this.log.info("[@DubboService] className:" + clazz.toString());
					Object obj = this.applicationContext.getBean(clazz);
					DubboService dubboService = (DubboService) annotation;
					String version = dubboService.version();
					int timeout = dubboService.timeout();
					boolean netty = dubboService.netty();
					boolean jetty = dubboService.jetty();
					boolean validation = dubboService.validation();
					if (null != obj)
					{
						for (Class cls:clazzInterfaces)
						{
							DubboFactory.registryService(cls, obj, version, timeout, netty, jetty, validation);
						}
					}
					break;
				}
			}

		}
	}


	private void initDubboConsumer(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		Field [] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			Annotation[] fieldAnnotations = field.getAnnotations();
			for (Annotation fieldAnnotation : fieldAnnotations)
			{
				if (fieldAnnotation instanceof DubboConsumer)
				{
					//String name = StringUtils.uncapitalize(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1));

					Object obj = this.applicationContext.getBean(clazz);
					if (null != obj)
					{
						DubboConsumer consumer = (DubboConsumer) fieldAnnotation;
						String version = consumer.version();
						int timeout = consumer.timeout();
						boolean retries = consumer.retries();
						String registryAddress = consumer.registry();
						boolean validation = consumer.validation();
						Object dubboConsumer = DubboFactory.getDubboService(field.getType(), version, timeout, retries, registryAddress, validation);
						this.log.info("[@DubboConsumer] className:"+field.getType().getName());
						field.setAccessible(true);
						field.set(obj, dubboConsumer);
					}
				}
			}
		}
	}

	public String getPackagePath()
	{
		return packagePath;
	}

	public void setPackagePath(String packagePath)
	{
		this.packagePath = packagePath;
	}

	public Properties getConf()
	{
		return conf;
	}

	public void setConf(Properties conf)
	{
		this.conf = conf;
	}

}

