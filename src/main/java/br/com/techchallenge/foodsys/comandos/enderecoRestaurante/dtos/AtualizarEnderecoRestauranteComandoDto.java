package br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos;

import lombok.Data;

@Data
public class AtualizarEnderecoRestauranteComandoDto {
    private String rua;
    private String cep;
    private String numero;
    private Long restauranteId;
}