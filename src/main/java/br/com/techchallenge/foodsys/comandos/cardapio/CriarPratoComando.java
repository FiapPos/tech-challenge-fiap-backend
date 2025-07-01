package br.com.techchallenge.foodsys.comandos.cardapio;
import java.math.BigDecimal;

public class CriarPratoComando {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean disponivelSomenteNoLocal;
    private String caminhoFoto;

    public CriarPratoComando(String nome, String descricao, BigDecimal preco,
                             boolean disponivelSomenteNoLocal, String caminhoFoto) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.caminhoFoto = caminhoFoto;
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

    public boolean isDisponivelSomenteNoLocal() {
        return disponivelSomenteNoLocal;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }
}
