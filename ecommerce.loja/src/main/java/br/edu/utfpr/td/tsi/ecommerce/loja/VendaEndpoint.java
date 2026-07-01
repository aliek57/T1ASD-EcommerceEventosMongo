package br.edu.utfpr.td.tsi.ecommerce.loja;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class VendaEndpoint {
	@Autowired
	private LojaPublisher p;
	ConexaoCliente cliente = new ConexaoCliente();
	private static final Logger logger = Logger.getLogger(VendaEndpoint.class.getName());
	
	@GetMapping("/catalogo")
    public String carregarCatalogo() {
        return cliente.buscarCatalogo();
    }
	
	@GetMapping("/cep/{cep}")
	public String consultarCep(@PathVariable String cep) {
	    return cliente.buscarCep(cep);
	}
	
	@PostMapping("/finalizar")
	public Venda finalizarVenda(@RequestBody Venda venda) {
		logger.log(Level.INFO, "Recebido novo pedido para {0}", venda.getNomeCliente());
		
		Gson gson = new Gson();
        String vendaJson = gson.toJson(venda);
        
        p.enviarEmail("{\"destinatario\":\"" + venda.getEmailCliente() + "\", "
        		+ "\"assunto\":\"Pedido Recebido\", \"mensagem\":\"Olá " + venda.getNomeCliente() + ", "
        		+ "seu pedido foi recebido e está aguardando pagamento.\"}");
	    
	    p.enviarPagamento(vendaJson);
	    
	    return venda;
	}
	
	@RabbitListener(queues = "fila.pagamento.dlq")
    public void processarErrosDLQ(String mensagemJson) {
        logger.log(Level.SEVERE, "Mensagem recebida da DLQ de Pagamentos: " + mensagemJson);
        
        try (FileWriter fw = new FileWriter("erros_dlq.log", true);
        PrintWriter pw = new PrintWriter(fw)) {
	        pw.println("--- ERRO DE PAGAMENTO REGISTRADO EM: " + java.time.LocalDateTime.now() + " ---");
	        pw.println(mensagemJson);
	        pw.println();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Falha crítica ao gravar arquivo de log: " + e.getMessage());
        }
    }
}