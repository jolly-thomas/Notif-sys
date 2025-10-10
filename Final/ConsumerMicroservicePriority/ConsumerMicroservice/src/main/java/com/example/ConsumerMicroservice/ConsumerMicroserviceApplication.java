package com.example.ConsumerMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ConsumerMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerMicroserviceApplication.class, args);
	}

}
