package br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante;

import lombok.Data;

@Data
public class DeletarEnderecoRestauranteComandoDto {
    private Long enderecoId;
    private Long restauranteId;
}
