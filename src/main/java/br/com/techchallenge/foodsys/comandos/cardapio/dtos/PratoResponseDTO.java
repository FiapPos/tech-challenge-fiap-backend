package br.com.techchallenge.foodsys.comandos.cardapio.dtos;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PratoResponseDTO {

    private Long id;
    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;
    private String descricao;
    @NotNull(message = "O preço é obrigatório")
    private BigDecimal preco;
    private Boolean disponivelSomenteNoLocal;
    private String caminhoFoto;

    public PratoResponseDTO(Long id, String nome, String descricao, BigDecimal preco,
                            Boolean disponivelSomenteNoLocal, String caminhoFoto) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelSomenteNoLocal = disponivelSomenteNoLocal;
        this.caminhoFoto = caminhoFoto;
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

    public static PratoResponseDTO fromEntity(Prato prato) {
        return new PratoResponseDTO(
                prato.getId(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.getDisponivelSomenteNoLocal(),
                prato.getCaminhoFoto()
        );
    }
}
