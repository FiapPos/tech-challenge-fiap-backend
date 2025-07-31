package br.com.techchallenge.foodsys.core.exceptions;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Usuário, senha ou tipo inválidos.");
    }
}