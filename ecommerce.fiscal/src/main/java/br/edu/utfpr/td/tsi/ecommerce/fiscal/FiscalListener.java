package br.edu.utfpr.td.tsi.ecommerce.fiscal;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class FiscalListener {
	private static final Logger logger = Logger.getLogger(FiscalListener.class.getName());
	
	@Autowired
	private FiscalPublisher p;

	@RabbitListener(queues = "fila.fiscal")
	public void emitirNotaFiscal(String mensagemJson) {
		try {
			JsonObject venda = JsonParser.parseString(mensagemJson).getAsJsonObject();
            String emailCliente = venda.get("emailCliente").getAsString();
            String nomeCliente = venda.get("nomeCliente").getAsString();
            String numeroNotaFiscal = UUID.randomUUID().toString();
			
			p.solicitarAgendamentoEntrega(mensagemJson);
			
			JsonObject emailJson = new JsonObject();
            emailJson.addProperty("destinatario", emailCliente);
            emailJson.addProperty("assunto", "NF Emitida");
            emailJson.addProperty("mensagem", "Olá " + nomeCliente + ", sua Nota Fiscal eletrônica foi gerada, código: " + numeroNotaFiscal);
            p.notificarEmail(emailJson.toString());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao emitir NF: " + e.getMessage(), e);
		}
	}
}