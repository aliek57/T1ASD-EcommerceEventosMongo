package br.edu.utfpr.td.tsi.ecommerce.loja;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LojaPublisher {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void enviarEmail(String mensagemJson) {
		rabbitTemplate.convertAndSend("fila.email", mensagemJson);
	}
	
	public void enviarPagamento(String pagamentoJson) {
		rabbitTemplate.convertAndSend("fila.pagamento", pagamentoJson);
	}
}