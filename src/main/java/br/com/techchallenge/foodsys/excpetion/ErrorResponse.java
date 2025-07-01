package br.com.techchallenge.foodsys.excpetion;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String mensagem;

    public ErrorResponse(String mensagem) {
        this.mensagem = mensagem;
    }
}