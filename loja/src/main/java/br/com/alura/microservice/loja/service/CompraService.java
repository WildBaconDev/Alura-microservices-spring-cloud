package br.com.alura.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.dto.InfoFornecedorDTO;

@Service
public class CompraService {

	@Autowired
	private RestTemplate client;
	
	public void realizaCompra(CompraDTO compra) {
		
		var exchange = client.exchange("http://fornecedor/info/" + compra.getEndereco().getEstado(), HttpMethod.GET, null, InfoFornecedorDTO.class );
		
		System.out.println(exchange.getBody().getEndereco());
		
	}

}
