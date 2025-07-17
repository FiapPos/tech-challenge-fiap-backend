package br.com.techchallenge.foodsys.dominio.restaurante;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "restaurante")

public class Restaurante {

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

    @Column(name = "usuario_dono_id", insertable = false, updatable = false)
    private Long usuarioDonoId;

    @OneToOne
    @JoinColumn(name = "usuario_dono_id")
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_desativacao")
    private LocalDateTime dataDesativacao;

}