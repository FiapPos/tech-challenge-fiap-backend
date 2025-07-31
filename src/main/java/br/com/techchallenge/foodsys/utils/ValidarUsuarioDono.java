package br.com.techchallenge.foodsys.utils;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
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
