package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidarProprietarioRestauranteTest {

    @Test
    void deveLancarExcecaoQuandoNaoForProprietarioRestaurante() {
        Usuario usuarioProprietario = new Usuario();
        usuarioProprietario.setId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setUsuario(usuarioProprietario);

        Long usuarioDonoId = 2L; // ID diferente do proprietário

        ValidarProprietarioRestaurante validador = new ValidarProprietarioRestaurante();

        assertThrows(
                BadRequestException.class,
                () -> validador.validarProprietario(restaurante, usuarioDonoId));
    }

    @Test
    void naoDeveLancarExcecaoQuandoForProprietarioRestaurante() {
        Usuario usuarioProprietario = new Usuario();
        usuarioProprietario.setId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setUsuario(usuarioProprietario);

        Long usuarioDonoId = 1L; // Mesmo ID do proprietário

        ValidarProprietarioRestaurante validador = new ValidarProprietarioRestaurante();

        assertDoesNotThrow(() -> validador.validarProprietario(restaurante, usuarioDonoId));
    }
}
