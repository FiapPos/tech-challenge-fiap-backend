package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import lombok.Data;

@Data
public class CriarEnderecoCommandDto {
    private String rua;
    private String cep;
    private String numero;
    private Long usuarioId;
}