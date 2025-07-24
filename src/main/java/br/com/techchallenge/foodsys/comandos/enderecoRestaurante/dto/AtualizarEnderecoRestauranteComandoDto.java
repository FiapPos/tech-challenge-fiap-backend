package br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarEnderecoRestauranteComandoDto {
    private String rua;
    private String cep;
    private String numero;
    @NotNull(message = "{restaurante.id.obrigatorio}")
    private Long restauranteId;
}