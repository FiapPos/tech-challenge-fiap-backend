package br.com.techchallenge.foodsys.dominio.usuario;

import br.com.techchallenge.foodsys.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private byte[] senha;

    @Column(name = "login", nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_desativacao")
    private LocalDateTime dataDesativacao;
}