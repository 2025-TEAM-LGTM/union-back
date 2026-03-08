package com.union.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnionApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnionApplication.class, args);
	}

}
