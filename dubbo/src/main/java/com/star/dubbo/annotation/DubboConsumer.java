package com.star.dubbo.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DubboConsumer
{
	String version() default "1.0.0";

	String registry() default "";

	int timeout() default 0;

	String timeoutExp() default "";

	boolean retries() default false;

	boolean validation() default false;

}
