package br.com.techchallenge.foodsys.comandos.cardapio.dtos;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemDoCardapioResponseDTO {

    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal preco;

    private Boolean disponivelSomenteNoLocal;

    private String caminhoFoto;

    @NotNull(message = "restauranteId é obrigatório")
    private Long restauranteId;

    public ItemDoCardapioResponseDTO(Long id, String nome, String descricao, BigDecimal preco,
                                     Boolean disponivelSomenteNoLocal, String caminhoFoto, Long restauranteId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.caminhoFoto = caminhoFoto;
        this.restauranteId = restauranteId;
    }

    public Long getId() {
        return id;
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

    public Long getRestauranteId() {
        return restauranteId;
    }

    public static ItemDoCardapioResponseDTO fromEntity(ItemDoCardapio itemDoCardapio) {
        return new ItemDoCardapioResponseDTO(
                itemDoCardapio.getId(),
                itemDoCardapio.getNome(),
                itemDoCardapio.getDescricao(),
                itemDoCardapio.getPreco(),
                itemDoCardapio.getDisponivelSomenteNoLocal(),
                itemDoCardapio.getCaminhoFoto(),
                itemDoCardapio.getRestaurante().getId()
        );
    }
}
