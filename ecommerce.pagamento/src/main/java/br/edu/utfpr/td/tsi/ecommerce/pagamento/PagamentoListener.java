package br.edu.utfpr.td.tsi.ecommerce.pagamento;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class PagamentoListener {
	private static final Logger logger = Logger.getLogger(PagamentoListener.class.getName());

	@Autowired
	private PagamentoPublisher p;

	@RabbitListener(queues = "fila.pagamento", ackMode = "MANUAL")
	public void processarPagamento(Message message, Channel channel) throws IOException {
		String in = new String(message.getBody());
		long deliveryTag = message.getMessageProperties().getDeliveryTag();

		try {
			JsonObject vendaJson = JsonParser.parseString(in).getAsJsonObject();
            java.math.BigDecimal valorTotal = vendaJson.get("valorTotal").getAsBigDecimal();
            String emailCliente = vendaJson.get("emailCliente").getAsString();
            String nomeCliente = vendaJson.get("nomeCliente").getAsString();

		    if (valorTotal != null && valorTotal.compareTo(java.math.BigDecimal.ZERO) < 0) {
		        logger.log(Level.WARNING, "Pagamento recusado! Enviando para DLQ: {0}", in);
		        channel.basicReject(deliveryTag, false);
		        return;
		    }

		    logger.log(Level.INFO, "Pagamento aprovado!");
		    
		    p.solicitarBaixaEstoque(in);
		    p.solicitarEmissaoNF(in);
		    
		    JsonObject emailJson = new JsonObject();
            emailJson.addProperty("destinatario", emailCliente);
            emailJson.addProperty("assunto", "Pagamento Confirmado");
            emailJson.addProperty("mensagem", "Olá " + nomeCliente + ", seu pagamento de R$ " + valorTotal + " foi processado com sucesso!");
            p.notificarEmail(emailJson.toString());

		    channel.basicAck(deliveryTag, false);

		} catch (Exception e) {
		    logger.log(Level.SEVERE, "Erro ao processar pagamento: " + e.getMessage(), e);
		    channel.basicReject(deliveryTag, false);
		}
	}
}