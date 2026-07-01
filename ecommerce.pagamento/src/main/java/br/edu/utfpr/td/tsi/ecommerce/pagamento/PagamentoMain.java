package br.edu.utfpr.td.tsi.ecommerce.pagamento;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PagamentoMain {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/ecommerce.pagamento");
		SpringApplication.run(PagamentoMain.class, args);
	}

	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void pagamentoQueue() {
		Queue filaPagamento = QueueBuilder.durable("fila.pagamento")
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", "fila.pagamento.dlq")
				.build();
		amqpAdmin.declareQueue(filaPagamento);

		Queue filaPagamentoDLQ = QueueBuilder.durable("fila.pagamento.dlq").build();
		amqpAdmin.declareQueue(filaPagamentoDLQ);
	}
}