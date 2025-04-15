package br.com.techchallenge.foodsys.comandos.login.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizaCredenciaisComandoDto {

    @NotNull(message = "{senha.nao.nula}")
    @Min(value = 8, message = "{senha.tamanho.minimo}")
    private String senha;

    @NotNull(message = "{confirmacao.senha.nao.nula}")
    @Min(value = 8, message = "{confirmacao.senha.tamanho.minimo}")
    private String confirmacaoSenha;
}
