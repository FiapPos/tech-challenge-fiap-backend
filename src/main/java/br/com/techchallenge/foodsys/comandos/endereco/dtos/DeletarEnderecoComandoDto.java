package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import lombok.Data;

@Data
public class DeletarEnderecoComandoDto {
    private Long enderecoId;
    private Long usuarioId;
    private Long restauranteId;
}
