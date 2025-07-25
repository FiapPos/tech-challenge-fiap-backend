package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class ValidarUsuarioDono {

    public void validarUsuarioDono(Usuario usuario) {
        boolean isAdmin = usuario.getUsuarioTipos().stream()
                .anyMatch(usuarioTipo -> TipoUsuario.DONO_RESTAURANTE.equals(usuarioTipo.getTipo()));

        if (!isAdmin) {
            throw new BadRequestException("usuario.nao.e.dono");
        }
    }

}
