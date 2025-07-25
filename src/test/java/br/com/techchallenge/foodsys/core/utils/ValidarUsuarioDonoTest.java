package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ValidarUsuarioDonoTest {

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTipo(TipoUsuario.CLIENTE);

        ValidarUsuarioDono validador = new ValidarUsuarioDono();

        assertThrows(
                BadRequestException.class,
                () -> validador.validarUsuarioDono(usuario));
    }

    @Test
    void naoDeveLancarExcecaoQuandoUsuarioDono() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setTipo(TipoUsuario.ADMIN);

        ValidarUsuarioDono validador = new ValidarUsuarioDono();
        assertDoesNotThrow(() -> validador.validarUsuarioDono(usuario));
    }
}
