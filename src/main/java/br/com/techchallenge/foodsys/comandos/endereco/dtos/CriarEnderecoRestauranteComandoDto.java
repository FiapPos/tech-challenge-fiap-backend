package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CriarEnderecoRestauranteComandoDto extends CriaEnderecoComandoDto {

    @NotNull(message = "{restauranteId.obrigatorio}")
    private Long restauranteId;
}