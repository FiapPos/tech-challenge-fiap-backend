package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioTipo;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ValidarUsuarioDonoTest {

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        ValidarUsuarioDono validador = new ValidarUsuarioDono();

        assertThrows(
                BadRequestException.class,
                () -> validador.validarUsuarioDono(usuario));
    }

    @Test
    void naoDeveLancarExcecaoQuandoUsuarioDono() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);

        UsuarioTipo usuarioTipoDono = new UsuarioTipo();
        usuarioTipoDono.setUsuario(usuario);
        usuarioTipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuario.getUsuarioTipos().add(usuarioTipoDono);

        ValidarUsuarioDono validador = new ValidarUsuarioDono();
        assertDoesNotThrow(() -> validador.validarUsuarioDono(usuario));
    }
}
