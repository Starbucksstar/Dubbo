package com.star.dubboconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.star")
public class DubboconsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboconsumerApplication.class, args);
	}
}
