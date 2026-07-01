package br.edu.utfpr.td.tsi.ecommerce.entrega;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class EntregaMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.entrega");
		SpringApplication.run(EntregaMain.class, args);
	}

	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void entregaQueue() {
		amqpAdmin.declareQueue(new Queue("fila.entrega", true));
	}
}