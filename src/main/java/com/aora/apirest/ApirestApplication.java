package com.aora.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;



@SpringBootApplication
public class ApirestApplication {

	public static void main(String[] args) {
		
		// Carga el archivo .env
		Dotenv dotenv = Dotenv.configure().load();
		
		// Opcional: Establece variables de entorno en el sistema (útil para `application.properties`)
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		
		
		SpringApplication.run(ApirestApplication.class, args);
	
	}

}
