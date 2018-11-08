package com.star.dubboprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.star")
public class DubboproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboproviderApplication.class, args);
	}
}
