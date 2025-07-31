package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import lombok.Data;

@Data
public abstract class AtualizarEnderecoComandoDto {
    private String rua;
    private String cep;
    private String numero;
}
