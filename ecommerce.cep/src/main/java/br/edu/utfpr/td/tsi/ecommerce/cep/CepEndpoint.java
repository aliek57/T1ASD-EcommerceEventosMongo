package br.edu.utfpr.td.tsi.ecommerce.cep;

import java.util.List;
import java.util.Arrays;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class CepEndpoint {
	private static List<Cep> enderecos;

	static {
		try {
			String arq = "enderecos.json";
			ObjectMapper mapper = new ObjectMapper();
			enderecos = Arrays.asList(mapper.readValue(new java.io.File(arq), Cep[].class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping(value = "/cep/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Cep buscarEndereco(@PathVariable String cep) {
		if (enderecos != null && !enderecos.isEmpty()) {
			int index = Math.abs(cep.hashCode()) % enderecos.size();
			Cep enderecoEncontrado = enderecos.get(index);
			enderecoEncontrado.setCep(cep);
			return enderecoEncontrado;
		}

		return new Cep(cep, "Rua Cristo Rei", "Vila Becker", 123, "Toledo", "PR");
	}
}