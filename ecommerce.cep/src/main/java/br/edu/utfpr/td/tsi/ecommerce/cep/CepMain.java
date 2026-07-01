package br.edu.utfpr.td.tsi.ecommerce.cep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CepMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.cep");
		SpringApplication.run(CepMain.class, args);
	}
}
