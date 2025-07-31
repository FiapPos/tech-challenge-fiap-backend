package br.com.techchallenge.foodsys.core.dtos.endereco;

import lombok.Data;

@Data
public class DeletarEnderecoComandoDto {
    private Long enderecoId;
    private Long usuarioId;
}
