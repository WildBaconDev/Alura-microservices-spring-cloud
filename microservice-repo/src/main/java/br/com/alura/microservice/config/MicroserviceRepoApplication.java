package br.com.alura.microservice.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MicroserviceRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRepoApplication.class, args);
	}

}
