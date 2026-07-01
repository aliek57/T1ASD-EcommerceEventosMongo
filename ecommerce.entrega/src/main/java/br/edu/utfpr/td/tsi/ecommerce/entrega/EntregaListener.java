package br.edu.utfpr.td.tsi.ecommerce.entrega;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class EntregaListener {
	private static final Logger logger = Logger.getLogger(EntregaListener.class.getName());
	
	@Autowired
    private EntregaPublisher p;

	@RabbitListener(queues = "fila.entrega")
	public void processarEntrega(String mensagemJson) {
		try {
			JsonObject venda = JsonParser.parseString(mensagemJson).getAsJsonObject();
            String cep = venda.get("cep").getAsString();
            String cliente = venda.get("nomeCliente").getAsString();
            String emailCliente = venda.get("emailCliente").getAsString();
            String codigoRastreio = "BR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String dataPrevisao = java.time.LocalDate.now().plusDays(7).toString();

            logger.log(Level.INFO, "Cliente: " + cliente + ", CEP: " + cep + ", código de rastreio: " + codigoRastreio);

            JsonObject emailJson = new JsonObject();
            emailJson.addProperty("destinatario", emailCliente);
            emailJson.addProperty("assunto", "Seu pedido foi Postado!");
            emailJson.addProperty("mensagem", "Olá " + cliente + ", seu pacote saiu para entrega. Código de rastreio: " + codigoRastreio + ". Entrega prevista até " + dataPrevisao);
            p.notificarEmail(emailJson.toString());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao processar agendamento de entrega: " + e.getMessage(), e);
		}
	}
}