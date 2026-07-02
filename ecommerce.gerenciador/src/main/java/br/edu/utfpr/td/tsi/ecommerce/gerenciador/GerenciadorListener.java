package br.edu.utfpr.td.tsi.ecommerce.gerenciador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class GerenciadorListener {

    private static final Logger logger = Logger.getLogger(GerenciadorListener.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queues = "fila.compra.concluida")
    public void processarSucesso(String mensagemJson) {
        logger.log(Level.INFO, "Mensagem recebida na fila de conclusão.");
        atualizarStatusMongo(mensagemJson, "Aprovado");
    }

    @RabbitListener(queues = "fila.pagamento.dlq")
    public void processarErrosDLQ(String mensagemJson) {
        logger.log(Level.SEVERE, "Mensagem recebida da DLQ de Pagamentos.");
        atualizarStatusMongo(mensagemJson, "Recusado");
        
        try (FileWriter fw = new FileWriter("erros_dlq.log", true);
            PrintWriter pw = new PrintWriter(fw)) {
	            pw.println("--- ERRO DE PAGAMENTO REGISTRADO EM: " + java.time.LocalDateTime.now() + " ---");
	            pw.println(mensagemJson);
	            pw.println();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Falha crítica ao gravar arquivo de log: " + e.getMessage());
        }
    }

    private void atualizarStatusMongo(String json, String novoStatus) {
        try {
            JsonObject vendaObj = JsonParser.parseString(json).getAsJsonObject();
            
            String idMongo = "";
            if (vendaObj.has("_id") && vendaObj.get("_id").isJsonObject()) {
                idMongo = vendaObj.getAsJsonObject("_id").get("$oid").getAsString();
            } else if (vendaObj.has("_id")) {
                idMongo = vendaObj.get("_id").getAsString();
            } else {
                logger.log(Level.WARNING, "ID não encontrado na mensagem.");
                return;
            }

            Query query = new Query(Criteria.where("_id").is(new ObjectId(idMongo)));
            Update update = new Update().set("statusPagamento", novoStatus);
            
            mongoTemplate.updateFirst(query, update, "vendas");
            logger.log(Level.INFO, "Status da venda {0} atualizado para: {1}", new Object[]{idMongo, novoStatus});
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao atualizar banco: " + e.getMessage(), e);
        }
    }
}