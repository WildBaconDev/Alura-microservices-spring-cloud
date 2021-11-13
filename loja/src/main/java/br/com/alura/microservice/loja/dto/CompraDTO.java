package br.com.alura.microservice.loja.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CompraDTO {
	@JsonIgnore
	private Long id;
	private List<ItemDaCompraDTO> itens;
	private EnderecoDTO endereco;
}
