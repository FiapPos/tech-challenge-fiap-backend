package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "login", nullable = false)
    private String login;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UsuarioTipoEntity> usuarioTipos = new HashSet<>();

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

    public Usuario toDomain() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setLogin(this.login);
        usuario.setAtivo(this.ativo);
        usuario.setDataCriacao(this.dataCriacao);
        usuario.setDataAtualizacao(this.dataAtualizacao);
        usuario.setDataDesativacao(this.dataDesativacao);
        
        // Converter UsuarioTipoEntity para UsuarioTipo sem criar loop
        Set<UsuarioTipo> tipos = this.usuarioTipos.stream()
            .map(entity -> {
                UsuarioTipo tipo = new UsuarioTipo();
                tipo.setId(entity.getId());
                tipo.setTipo(entity.getTipo());
                tipo.setUsuario(usuario); // Referência direta para evitar loop
                return tipo;
            })
            .collect(Collectors.toSet());
        usuario.setUsuarioTipos(tipos);
        
        return usuario;
    }

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setSenha(usuario.getSenha());
        entity.setLogin(usuario.getLogin());
        entity.setAtivo(usuario.isAtivo());
        entity.setDataCriacao(usuario.getDataCriacao());
        entity.setDataAtualizacao(usuario.getDataAtualizacao());
        entity.setDataDesativacao(usuario.getDataDesativacao());
        
        // Converter UsuarioTipo para UsuarioTipoEntity sem criar loop
        Set<UsuarioTipoEntity> tipos = usuario.getUsuarioTipos().stream()
            .map(tipo -> {
                UsuarioTipoEntity tipoEntity = new UsuarioTipoEntity();
                tipoEntity.setId(tipo.getId());
                tipoEntity.setTipo(tipo.getTipo());
                tipoEntity.setUsuario(entity); // Referência direta para evitar loop
                return tipoEntity;
            })
            .collect(Collectors.toSet());
        entity.setUsuarioTipos(tipos);
        
        return entity;
    }
} 