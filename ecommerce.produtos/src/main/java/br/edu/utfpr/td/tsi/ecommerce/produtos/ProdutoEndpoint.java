package br.edu.utfpr.td.tsi.ecommerce.produtos;

import java.util.List;
import java.util.Arrays;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ProdutoEndpoint {
	private static List<Produto> produtos;

	static {
	    try {
	    	String arq = "produtos.json";
	        ObjectMapper mapper = new ObjectMapper();
	        produtos = Arrays.asList(mapper.readValue(new java.io.File(arq), Produto[].class));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
	
	@GetMapping("/catalogo")
    public List<Produto> carregarCatalogo() {
        return produtos;
    }

    public static void baixarEstoque(String id) {
	    for (Produto p : produtos) {
	        if (p.getId().equals(id)) {
	            p.setQuantidadeEmEstoque(p.getQuantidadeEmEstoque() - 1);
	            break;
	        }
	    }
    }
}
