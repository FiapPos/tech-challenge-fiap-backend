package br.com.techchallenge.foodsys.core.domain.entities;

import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = "usuario")
@ToString(exclude = "usuario")
public class UsuarioTipo {

    private Long id;
    private Usuario usuario;
    private TipoUsuario tipo;

    public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(tipo);
    }

    public boolean isCliente() {
        return TipoUsuario.CLIENTE.equals(tipo);
    }

    public boolean isFuncionario() {
        return TipoUsuario.FUNCIONARIO.equals(tipo);
    }

    public boolean isDonoRestaurante() {
        return TipoUsuario.DONO_RESTAURANTE.equals(tipo);
    }
}