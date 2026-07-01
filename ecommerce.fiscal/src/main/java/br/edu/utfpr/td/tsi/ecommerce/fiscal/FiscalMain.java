package br.edu.utfpr.td.tsi.ecommerce.fiscal;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class FiscalMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.fiscal");
		SpringApplication.run(FiscalMain.class, args);
	}

	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void fiscalQueue() {
		amqpAdmin.declareQueue(new Queue("fila.fiscal", true));
	}
}