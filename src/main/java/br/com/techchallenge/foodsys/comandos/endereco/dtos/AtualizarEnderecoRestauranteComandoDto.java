package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AtualizarEnderecoRestauranteComandoDto extends AtualizarEnderecoComandoDto {
    @NotNull(message = "{restaurante.id.obrigatorio}")
    private Long restauranteId;
}