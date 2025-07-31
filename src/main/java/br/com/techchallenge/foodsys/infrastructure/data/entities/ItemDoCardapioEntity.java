package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "item_do_cardapio")
public class ItemDoCardapioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private String descricao;

    private BigDecimal preco;

    private Boolean disponivelSomenteNoLocal;

    private String caminhoFoto;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntity restaurante;

    public ItemDoCardapio toDomain() {
        ItemDoCardapio item = new ItemDoCardapio();
        item.setId(this.id);
        item.setNome(this.nome);
        item.setDescricao(this.descricao);
        item.setPreco(this.preco);
        item.setDisponivelSomenteNoLocal(this.disponivelSomenteNoLocal);
        item.setCaminhoFoto(this.caminhoFoto);
        if (this.restaurante != null) {
            item.setRestaurante(this.restaurante.toDomain());
        }
        return item;
    }

    public static ItemDoCardapioEntity fromDomain(ItemDoCardapio item) {
        ItemDoCardapioEntity entity = new ItemDoCardapioEntity();
        entity.setId(item.getId());
        entity.setNome(item.getNome());
        entity.setDescricao(item.getDescricao());
        entity.setPreco(item.getPreco());
        entity.setDisponivelSomenteNoLocal(item.getDisponivelSomenteNoLocal());
        entity.setCaminhoFoto(item.getCaminhoFoto());
        if (item.getRestaurante() != null) {
            entity.setRestaurante(RestauranteEntity.fromDomain(item.getRestaurante()));
        }
        return entity;
    }

    public RestauranteEntity getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(RestauranteEntity restaurante) {
        this.restaurante = restaurante;
    }

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