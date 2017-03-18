package org.mattreyuk.adtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class AdtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdtechApplication.class, args);
	}
}
