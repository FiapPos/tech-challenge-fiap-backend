package br.com.techchallenge.foodsys.comandos.cardapio;
import java.math.BigDecimal;

public class CriarPratoComando {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelSomenteNoLocal;
    private String caminhoFoto;
    private Long restauranteId;

    public CriarPratoComando(String nome, String descricao, BigDecimal preco,
                             Boolean disponivelSomenteNoLocal, String caminhoFoto, Long restauranteId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.caminhoFoto = caminhoFoto;
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

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public Long getRestauranteId() {return restauranteId; }
}
