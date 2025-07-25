package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;

import java.math.BigDecimal;

public class AtualizarItemDoCardapioComando {

    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private final Boolean disponivelSomenteNoLocal;


    public AtualizarItemDoCardapioComando(String nome, String descricao, BigDecimal preco,
                                          Boolean disponivelSomenteNoLocal) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;

    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getDisponivelSomenteNoLocal() {
        return disponivelSomenteNoLocal;
    }

    public void aplicarEm(ItemDoCardapio item) {
        if (this.nome != null) {
            item.setNome(this.nome);
        }
        if (this.descricao != null) {
            item.setDescricao(this.descricao);
        }
        if (this.preco != null) {
            item.setPreco(this.preco);
        }
        if (this.disponivelSomenteNoLocal != null) {
            item.setDisponivelSomenteNoLocal(this.disponivelSomenteNoLocal);
        }
    }

}
