package br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dto;

import lombok.Data;

@Data
public class DeletarEnderecoRestauranteComandoDto {
    private Long enderecoId;
    private Long restauranteId;
}
