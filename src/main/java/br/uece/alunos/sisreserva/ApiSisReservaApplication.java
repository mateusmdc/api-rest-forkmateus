package br.uece.alunos.sisreserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiSisReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSisReservaApplication.class, args);
	}

}
