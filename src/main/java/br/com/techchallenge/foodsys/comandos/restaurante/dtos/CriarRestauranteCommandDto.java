package br.com.techchallenge.foodsys.comandos.restaurante.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class CriarRestauranteCommandDto {

    @NotBlank(message = "{nome.obrigatorio}")
    private String nome;

    @NotBlank(message = "{endereco.obrigatorio}")
    private String endereco;

    @NotBlank(message = "{tipoCozinha.obrigatorio}")
    private String tipoCozinha;

    @NotNull(message = "{usuarioDonoId.obrigatorio}")
    private Long usuarioDonoId;

    @NotBlank(message = "{horarioFuncionamento.obrigatorio}")
    private String horarioFuncionamento;

}
