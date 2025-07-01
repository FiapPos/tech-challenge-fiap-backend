package br.com.techchallenge.foodsys.comandos.restaurante.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarRestauranteComandoDto {
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    @NotNull(message = "{usuarioDonoId.obrigatorio}")
    private Long usuarioDonoId;
}