package br.edu.utfpr.td.tsi.ecommerce.gerenciador;

import org.bson.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.FullDocument;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Monitor implements CommandLineRunner {

	@Autowired
	private RabbitTemplate rabbit;
	
	private static final Logger logger = Logger.getLogger(Monitor.class.getName());

	@Override
	public void run(String... args) throws Exception {
		
		new Thread(() -> {
			logger.log(Level.INFO, "Iniciando Monitorador do MongoDB");
			
			try (MongoClient client = MongoClients.create("mongodb://127.0.0.1:27017")) {
				MongoDatabase db = client.getDatabase("ecommerce");
				MongoCollection<Document> collection = db.getCollection("vendas");
				
				collection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).forEach(event -> {
					Document doc = event.getFullDocument();
					
					logger.log(Level.INFO, "Operação {0}", event.getOperationType());
					
					if (doc != null && event.getOperationType().getValue().equals("insert")) {
						String vendaJson = doc.toJson();
						
						logger.log(Level.INFO, "Enviando para fila");
						
						rabbit.convertAndSend("fila.pagamento", vendaJson);
					}
				});
				
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Erro no monitorador: " + e.getMessage());
			}
		}).start();
	}
}