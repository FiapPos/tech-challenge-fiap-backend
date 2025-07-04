package br.com.techchallenge.foodsys.comandos.cardapio;
import java.math.BigDecimal;

public class AtualizarPratoComando {

    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private final Boolean disponivelSomenteNoLocal;


    public AtualizarPratoComando(String nome, String descricao, BigDecimal preco,
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

}
