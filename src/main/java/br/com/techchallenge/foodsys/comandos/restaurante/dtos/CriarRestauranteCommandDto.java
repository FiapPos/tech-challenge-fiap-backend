package br.com.techchallenge.foodsys.comandos.restaurante.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class CriarRestauranteCommandDto {

    @NotBlank(message = "{nome.obrigatorio}")
    private String nome;

    @NotBlank(message = "{tipoCozinha.obrigatorio}")
    private String tipoCozinha;

    @NotBlank(message = "{horarioAbertura.obrigatorio}")
    private String horarioAbertura;

    @NotBlank(message = "{horarioFechamento.obrigatorio}")
    private String horarioFechamento;

}
