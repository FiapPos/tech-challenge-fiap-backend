package br.com.techchallenge.foodsys.core.domain.entities;

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
public class UsuarioTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoUsuario tipo;
}