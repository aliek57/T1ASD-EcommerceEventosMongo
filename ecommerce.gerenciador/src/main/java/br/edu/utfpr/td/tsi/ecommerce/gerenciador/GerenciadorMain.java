package br.edu.utfpr.td.tsi.ecommerce.gerenciador;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class GerenciadorMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.gerenciador");
		SpringApplication.run(GerenciadorMain.class, args);
	}
	
	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void gerenciadorQueue() {
		amqpAdmin.declareQueue(new Queue("fila.compra.concluida", true));
	}
}