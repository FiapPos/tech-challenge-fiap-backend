package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rua")
    private String rua;

    @Column(name = "cep")
    private String cep;

    @Column(name = "numero")
    private String numero;

    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private RestauranteEntity restaurante;

    public Endereco toDomain() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setRua(this.rua);
        endereco.setCep(this.cep);
        endereco.setNumero(this.numero);
        endereco.setUsuarioId(this.usuarioId);
        if (this.usuario != null) {
            endereco.setUsuario(this.usuario.toDomain());
        }
        endereco.setDataCriacao(this.dataCriacao);
        endereco.setDataAtualizacao(this.dataAtualizacao);
        if (this.restaurante != null) {
            endereco.setRestaurante(this.restaurante.toDomain());
        }
        return endereco;
    }

    public static EnderecoEntity fromDomain(Endereco endereco) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(endereco.getId());
        entity.setRua(endereco.getRua());
        entity.setCep(endereco.getCep());
        entity.setNumero(endereco.getNumero());
        entity.setUsuarioId(endereco.getUsuarioId());
        if (endereco.getUsuario() != null) {
            entity.setUsuario(UsuarioEntity.fromDomain(endereco.getUsuario()));
        }
        entity.setDataCriacao(endereco.getDataCriacao());
        entity.setDataAtualizacao(endereco.getDataAtualizacao());
        if (endereco.getRestaurante() != null) {
            entity.setRestaurante(RestauranteEntity.fromDomain(endereco.getRestaurante()));
        }
        return entity;
    }
} 