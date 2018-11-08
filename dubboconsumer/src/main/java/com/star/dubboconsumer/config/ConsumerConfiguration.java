package com.star.dubboconsumer.config;

import com.star.dubbo.spring.DubboAnnotationBeanParser;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ConsumerConfiguration
{
	@Bean
	public DubboAnnotationBeanParser dubboAnnotationBeanParser(){
		return new DubboAnnotationBeanParser();
	}

}
