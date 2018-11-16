package com.star.dubboprovider;

import com.star.dubboprovider.bean.MoreThread;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@SpringBootApplication
@ComponentScan(basePackages = "com.star")
public class DubboproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboproviderApplication.class, args);
	}
}
