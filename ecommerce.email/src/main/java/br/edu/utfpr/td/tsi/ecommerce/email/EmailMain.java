package br.edu.utfpr.td.tsi.ecommerce.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.email");
		SpringApplication.run(EmailMain.class, args);
	}
}