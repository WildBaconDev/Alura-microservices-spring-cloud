package br.com.alura.microservice.loja.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.client.TransportadorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.dto.EntregaDTO;
import br.com.alura.microservice.loja.dto.VoucherDTO;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.model.CompraState;
import br.com.alura.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {
	
	private static final Logger log = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private FornecedorClient fornecedorClient;
	
	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private TransportadorClient transportadorClient;
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallback")
	public Compra realizaCompra(CompraDTO compra) {
		
		Compra compraSalva = new Compra();
		compraSalva.setState( CompraState.RECEBIDO );
		compraSalva.setEnderecoDestino( compra.getEndereco().toString() );
		compraRepository.save(compraSalva);
		compra.setId( compraSalva.getId() );
				
		log.info("realizando um pedido");
		var pedido = fornecedorClient.realizaPedido(compra.getItens());
		compraSalva.setState( CompraState.PEDIDO_REALIZADO );
		compraSalva.setPedidoId( pedido.getId() );
		compraSalva.setTempoDePreparo( pedido.getTempoDePreparo() );
		compraRepository.save(compraSalva);
		
		var info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		var entregaDto = new EntregaDTO();
		entregaDto.setPedidoId(pedido.getId());
		entregaDto.setDataParaEntrega(LocalDate.now().plusDays( pedido.getTempoDePreparo() ));
		entregaDto.setEnderecoOrigem( info.getEndereco() );
		entregaDto.setEnderecoDestino( compra.getEndereco().toString() );
		VoucherDTO voucher = transportadorClient.reservaEntrega(entregaDto);

		compraSalva.setState( CompraState.RESERVA_ENTREGA_REALIZADA );
		compraSalva.setDataParaEntrega( voucher.getPrevisaoParaEntrega() );
		compraSalva.setVoucher( voucher.getNumero() );
		compraRepository.save(compraSalva);
		
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallback(CompraDTO compra) {
		if (compra.getId() != null) {			
			return compraRepository.findById( compra.getId() ).get();
		}
		
		return null;
	}

	@HystrixCommand
	public Optional<Compra> getById(Long id) {
		return compraRepository.findById(id);
	}

}
