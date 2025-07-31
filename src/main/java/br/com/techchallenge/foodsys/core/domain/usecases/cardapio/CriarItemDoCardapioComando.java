package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import java.math.BigDecimal;

public class CriarItemDoCardapioComando {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelSomenteNoLocal;
    private Long restauranteId;

    public CriarItemDoCardapioComando(String nome, String descricao, BigDecimal preco,
                                      Boolean disponivelSomenteNoLocal, Long restauranteId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.restauranteId = restauranteId;
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

    public Long getRestauranteId() {return restauranteId; }
}
