package br.com.alura.microservice.loja.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {
	
	private static final Logger log = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private FornecedorClient fornecedorClient;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallback")
	public Compra realizaCompra(CompraDTO compra) {
		
		log.info("realizando um pedido");
		var pedido = fornecedorClient.realizaPedido(compra.getItens());

		Compra compraSalva = new Compra();
		compraSalva.setPedidoId( pedido.getId() );
		compraSalva.setTempoDePreparo( pedido.getTempoDePreparo() );
		compraSalva.setEnderecoDestino( compra.getEndereco().toString() );
		compraRepository.save(compraSalva);
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallback(CompraDTO compra) {
		var compraFallback = new Compra();
		compraFallback.setEnderecoDestino(compra.getEndereco().toString());
		return compraFallback;
	}

	@HystrixCommand
	public Optional<Compra> getById(Long id) {
		return compraRepository.findById(id);
	}

}
