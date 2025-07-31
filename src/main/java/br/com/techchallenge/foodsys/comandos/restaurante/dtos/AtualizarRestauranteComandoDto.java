package br.com.techchallenge.foodsys.comandos.restaurante.dtos;

import lombok.Data;

@Data
public class AtualizarRestauranteComandoDto {
    private String nome;
    private String tipoCozinha;
    private String horarioAbertura;
    private String horarioFechamento;
}