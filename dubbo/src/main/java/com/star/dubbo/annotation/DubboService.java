package com.star.dubbo.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DubboService
{
	String version() default "1.0.0";

	int timeout() default 0;

	boolean netty() default true;

	boolean jetty() default false;

	boolean validation() default false;
}
