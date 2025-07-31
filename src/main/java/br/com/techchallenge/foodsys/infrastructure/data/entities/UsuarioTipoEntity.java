package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "usuario_tipo")
@EqualsAndHashCode(exclude = "usuario")
@ToString(exclude = "usuario")
public class UsuarioTipoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoUsuario tipo;

    public UsuarioTipo toDomain() {
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setId(this.id);
        usuarioTipo.setTipo(this.tipo);
        // Não definir usuario aqui para evitar loop
        return usuarioTipo;
    }

    public static UsuarioTipoEntity fromDomain(UsuarioTipo usuarioTipo) {
        UsuarioTipoEntity entity = new UsuarioTipoEntity();
        entity.setId(usuarioTipo.getId());
        entity.setTipo(usuarioTipo.getTipo());
        // Não definir usuario aqui para evitar loop
        return entity;
    }
} 