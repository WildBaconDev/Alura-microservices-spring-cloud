package br.com.alura.microservice.loja.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompraDTO {
	private List<ItemDaCompraDTO> itens;
	private EnderecoDTO endereco;
}
