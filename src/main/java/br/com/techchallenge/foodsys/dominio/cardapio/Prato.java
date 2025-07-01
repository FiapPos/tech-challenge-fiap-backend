package br.com.techchallenge.foodsys.dominio.cardapio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "cardapio")
public class Prato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private String descricao;

    private BigDecimal preco;


    private Boolean disponivelSomenteNoLocal;

    private String caminhoFoto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
