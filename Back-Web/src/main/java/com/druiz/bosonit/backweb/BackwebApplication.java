package com.druiz.bosonit.backweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class BackwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackwebApplication.class, args);
	}

}
