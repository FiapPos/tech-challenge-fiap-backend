package br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarEnderecoRestauranteComandoDto {

    @NotBlank(message = "{rua.obrigatoria}")
    private String rua;

    @NotBlank(message = "{cep.obrigatorio}")
    private String cep;

    @NotBlank(message = "{numero.obrigatorio}")
    private String numero;

    @NotNull(message = "{restauranteId.obrigatorio}")
    private Long restauranteId;
}