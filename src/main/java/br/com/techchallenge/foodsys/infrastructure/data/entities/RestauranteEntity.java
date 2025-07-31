package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "restaurante")
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo_cozinha")
    private String tipoCozinha;

    @Column(name = "horario_abertura")
    private String horarioAbertura;

    @Column(name = "horario_fechamento")
    private String horarioFechamento;

    @Column(name = "ativo")
    private boolean ativo = true;

    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_desativacao")
    private LocalDateTime dataDesativacao;

    public Restaurante toDomain() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(this.id);
        restaurante.setNome(this.nome);
        restaurante.setTipoCozinha(this.tipoCozinha);
        restaurante.setHorarioAbertura(this.horarioAbertura);
        restaurante.setHorarioFechamento(this.horarioFechamento);
        restaurante.setAtivo(this.ativo);
        restaurante.setUsuarioId(this.usuarioId);
        if (this.usuario != null) {
            restaurante.setUsuario(this.usuario.toDomain());
        }
        restaurante.setDataCriacao(this.dataCriacao);
        restaurante.setDataAtualizacao(this.dataAtualizacao);
        restaurante.setDataDesativacao(this.dataDesativacao);
        return restaurante;
    }

    public static RestauranteEntity fromDomain(Restaurante restaurante) {
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(restaurante.getId());
        entity.setNome(restaurante.getNome());
        entity.setTipoCozinha(restaurante.getTipoCozinha());
        entity.setHorarioAbertura(restaurante.getHorarioAbertura());
        entity.setHorarioFechamento(restaurante.getHorarioFechamento());
        entity.setAtivo(restaurante.isAtivo());
        entity.setUsuarioId(restaurante.getUsuarioId());
        if (restaurante.getUsuario() != null) {
            entity.setUsuario(UsuarioEntity.fromDomain(restaurante.getUsuario()));
        }
        entity.setDataCriacao(restaurante.getDataCriacao());
        entity.setDataAtualizacao(restaurante.getDataAtualizacao());
        entity.setDataDesativacao(restaurante.getDataDesativacao());
        return entity;
    }
} 