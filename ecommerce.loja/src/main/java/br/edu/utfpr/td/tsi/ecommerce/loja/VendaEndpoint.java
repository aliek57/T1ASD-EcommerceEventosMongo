package br.edu.utfpr.td.tsi.ecommerce.loja;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

@RestController
@CrossOrigin
public class VendaEndpoint {
	@Autowired
	private LojaPublisher p;
	
	@Autowired
	private MongoTemplate mongo;
	
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
		logger.log(Level.INFO, "Sistema Legado - MongoDB");
		logger.log(Level.INFO, "Recebido novo pedido para {0}", venda.getNomeCliente());
        
        p.enviarEmail("{\"destinatario\":\"" + venda.getEmailCliente() + "\", "
        		+ "\"assunto\":\"Pedido Recebido\", \"mensagem\":\"Olá " + venda.getNomeCliente() + ", "
        		+ "seu pedido foi recebido e está aguardando pagamento.\"}");
	    
        mongo.save(venda, "vendas");
        logger.log(Level.INFO, "Venda salva!");
	    
	    return venda;
	}
}