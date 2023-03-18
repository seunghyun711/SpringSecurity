package com.example.SpringSecurityPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class SpringSecurityPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityPracticeApplication.class, args);
	}

}
