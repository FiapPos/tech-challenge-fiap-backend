package br.com.techchallenge.foodsys.excpetion;

public class ItemDoCardapioNaoEncontradoException extends RuntimeException {

    public ItemDoCardapioNaoEncontradoException(Long id) {
        super("Item do cardápio não encontrado com id: " + id);
    }
}