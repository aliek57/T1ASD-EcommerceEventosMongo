package br.edu.utfpr.td.tsi.ecommerce.produtos;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class ProdutosListener {

    private static final Logger logger = Logger.getLogger(ProdutosListener.class.getName());

    @RabbitListener(queues = "fila.estoque")
    public void processarBaixa(String mensagemJson) {
    	try {
			JsonObject venda = new Gson().fromJson(mensagemJson, JsonObject.class);
			JsonArray itens = venda.getAsJsonArray("itens");

			if (itens != null) {
				itens.forEach(itemElement -> {
					JsonObject item = itemElement.getAsJsonObject();
					String idProduto = "";
					if (item.has("_id")) {
						idProduto = item.get("_id").getAsString();
					} else if (item.has("id")) {
						idProduto = item.get("id").getAsString();
					};
					
					ProdutoEndpoint.baixarEstoque(idProduto);
				});
				logger.log(Level.INFO, "Estoque atualizado!");
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao processar baixa de estoque: " + e.getMessage(), e);
		}
    }
}