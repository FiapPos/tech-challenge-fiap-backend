package br.com.techchallenge.foodsys.excpetion;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Usuário, senha ou tipo inválidos.");
    }
}