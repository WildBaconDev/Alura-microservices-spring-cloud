package br.com.alura.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.model.Compra;

@Service
public class CompraService {
	
	private static final Logger log = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private FornecedorClient fornecedorClient;
	
	public Compra realizaCompra(CompraDTO compra) {
		
		final String estado = compra.getEndereco().getEstado();
		
		log.info("buscando informações do fornecedor de {}", estado);
		var info = fornecedorClient.getInfoPorEstado(estado);
		
		log.info("realizando um pedido");
		var pedido = fornecedorClient.realizaPedido(compra.getItens());

		Compra compraSalva = new Compra();
		compraSalva.setPedidoId( pedido.getId() );
		compraSalva.setTempoDePreparo( pedido.getTempoDePreparo() );
		compraSalva.setEnderecoDestino( compra.getEndereco().toString() );
		
		
		return compraSalva;
	}

}
