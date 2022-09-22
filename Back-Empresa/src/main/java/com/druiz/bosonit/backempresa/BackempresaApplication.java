package com.druiz.bosonit.backempresa;

import com.druiz.bosonit.backempresa.user.application.UserService;
import com.druiz.bosonit.backempresa.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class BackempresaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackempresaApplication.class, args);
	}

	@Autowired
	UserService userService;

	@Bean
	CommandLineRunner testUsers() {
		return p -> {
			User admin = userService.findUserByMailAndByPassword("admin@virtualtravel.com", "admin");
			if (admin == null) {
				User adminUser = new User(null, "admin@virtualtravel.com", "admin", "ROLE_ADMIN");
				userService.saveUser(adminUser);
			}
		};
	}

}
