package br.com.techchallenge.foodsys.comandos.cardapio.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PratoRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal preco;

    private Boolean disponivelSomenteNoLocal;

    private String caminhoFoto;

    @NotNull(message = "{restauranteId.obrigatorio}")
    private Long restauranteId;

    public PratoRequestDTO(String nome, String descricao, BigDecimal preco,
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

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
}
