package br.com.techchallenge.foodsys.comandos.cardapio.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemDoCardapioRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal preco;

    private Boolean disponivelSomenteNoLocal;

    public ItemDoCardapioRequestDTO() {
    }

    public ItemDoCardapioRequestDTO(String nome, String descricao, BigDecimal preco,
                                    Boolean disponivelSomenteNoLocal) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivelSomenteNoLocal() {
        return disponivelSomenteNoLocal;
    }

    public void setDisponivelSomenteNoLocal(Boolean disponivelSomenteNoLocal) {
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
    }

}
