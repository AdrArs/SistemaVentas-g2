package com.codigo.permit_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients("com.codigo.*")
@EnableJpaRepositories("com.codigo")
@ComponentScan("com.codigo.*")
@EntityScan("com.codigo.*")
public class PermitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PermitServiceApplication.class, args);
	}

}
