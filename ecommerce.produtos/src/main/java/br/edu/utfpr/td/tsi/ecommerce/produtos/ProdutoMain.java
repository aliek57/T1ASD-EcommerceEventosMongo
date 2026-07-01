package br.edu.utfpr.td.tsi.ecommerce.produtos;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProdutoMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.produtos");
		SpringApplication.run(ProdutoMain.class, args);
	}
	
	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void produtoQueue() {
	    amqpAdmin.declareQueue(new Queue("fila.estoque", true));
	}
}