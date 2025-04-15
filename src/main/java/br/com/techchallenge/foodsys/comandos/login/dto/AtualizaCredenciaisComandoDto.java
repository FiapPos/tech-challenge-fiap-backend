package br.com.techchallenge.foodsys.comandos.login.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizaCredenciaisComandoDto(

        @NotNull(message = "{senha.nao.nula}")
        @Min(value = 8, message = "{senha.tamanho.minimo}")
        String senha,

        @NotNull(message = "{confirmacao.senha.nao.nula}")
        @Min(value = 8, message = "{confirmacao.senha.tamanho.minimo}")
        String confirmacaoSenha

) {}
