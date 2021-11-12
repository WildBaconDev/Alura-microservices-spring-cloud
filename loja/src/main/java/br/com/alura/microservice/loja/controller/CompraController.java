package br.com.alura.microservice.loja.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.service.CompraService;

@RestController
@RequestMapping("/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@PostMapping
	public Compra realizaCompra( @RequestBody CompraDTO compra ) {
		return compraService.realizaCompra(compra);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Compra> getById( @PathVariable("id") Long id ) {
		Optional<Compra> compra = compraService.getById(id);
		
		if (compra.isPresent()) {
			return ResponseEntity.ok( compra.get() );
		}
		
		return ResponseEntity.notFound().build();
	}
	
}
