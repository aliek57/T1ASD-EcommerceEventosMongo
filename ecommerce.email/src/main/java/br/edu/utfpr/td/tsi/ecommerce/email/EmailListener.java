package br.edu.utfpr.td.tsi.ecommerce.email;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

	private static final Logger logger = Logger.getLogger(EmailListener.class.getName());

	@RabbitListener(queues = "fila.email")
	public void escutarFilaEmail(String conteudo) {
		logger.log(Level.INFO, "Mensagem recebida na fila.email: {0}", conteudo);
	}
}