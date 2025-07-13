package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ValidarProprietarioEnderecoTest {

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setRestaurante(restaurante);

        Long restauranteIdDiferente = 2L;

        ValidarProprietarioEndereco validador = new ValidarProprietarioEndereco();

        assertThrows(
                BadRequestException.class,
                () -> validador.validarProprietarioEndereco(endereco, null, restauranteIdDiferente));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEnderecoPertenceAoRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setRestaurante(restaurante);

        Long restauranteId = 1L;

        ValidarProprietarioEndereco validador = new ValidarProprietarioEndereco();

        assertDoesNotThrow(
                () -> validador.validarProprietarioEndereco(endereco, null, restauranteId));
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoUsuario() {
        Long usuarioIdEndereco = 1L;
        Long usuarioIdDiferente = 2L;

        br.com.techchallenge.foodsys.dominio.usuario.Usuario usuario = new br.com.techchallenge.foodsys.dominio.usuario.Usuario();
        usuario.setId(usuarioIdEndereco);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);

        ValidarProprietarioEndereco validador = new ValidarProprietarioEndereco();

        assertThrows(
                BadRequestException.class,
                () -> validador.validarProprietarioEndereco(endereco, usuarioIdDiferente, null));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEnderecoPertenceAoUsuario() {
        Long usuarioId = 1L;

        br.com.techchallenge.foodsys.dominio.usuario.Usuario usuario = new br.com.techchallenge.foodsys.dominio.usuario.Usuario();
        usuario.setId(usuarioId);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);

        ValidarProprietarioEndereco validador = new ValidarProprietarioEndereco();

        assertDoesNotThrow(
                () -> validador.validarProprietarioEndereco(endereco, usuarioId, null));
    }
}