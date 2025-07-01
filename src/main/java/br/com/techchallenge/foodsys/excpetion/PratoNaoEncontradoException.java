package br.com.techchallenge.foodsys.excpetion;

public class PratoNaoEncontradoException extends RuntimeException {

    public PratoNaoEncontradoException(Long id) {
        super("Prato não encontrado com id: " + id);
    }
}