package com.star.dubboprovider.config;

import com.star.dubbo.spring.DubboAnnotationBeanParser;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration
{
	@Bean
	public DubboAnnotationBeanParser getDubboAnnotationBeanParser(){
		return new DubboAnnotationBeanParser();
	}

}
