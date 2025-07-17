package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarDadosDelecaoEnderecoRestauranteTest {

        @Mock
        private ValidarUsuarioExistente validarUsuarioExistente;
        @Mock
        private ValidarEnderecoExistente validarEnderecoExistente;
        @Mock
        private ValidarProprietarioEndereco validarProprietarioEndereco;

        private ValidarDadosDelecaoEnderecoRestaurante validarDadosDelecaoEndereco;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                validarDadosDelecaoEndereco = new ValidarDadosDelecaoEnderecoRestaurante(
                                validarUsuarioExistente,
                                validarEnderecoExistente,
                                validarProprietarioEndereco);
        }

        @Test
        void deveValidarDelecaoComSucesso() {
                Long enderecoId = 1L;
                Long usuarioId = 2L;
                Long restauranteId = 3L;

                Endereco endereco = new Endereco();
                endereco.setId(enderecoId);

                when(validarUsuarioExistente.execute(usuarioId)).thenReturn(null);
                when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
                doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId,
                                restauranteId);

                Endereco resultado = validarDadosDelecaoEndereco.validarDelecao(enderecoId, usuarioId, restauranteId);

                assertNotNull(resultado);
                assertEquals(enderecoId, resultado.getId());
                verify(validarUsuarioExistente).execute(usuarioId);
                verify(validarEnderecoExistente).execute(enderecoId);
                verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
                Long enderecoId = 1L;
                Long usuarioId = 2L;
                Long restauranteId = null;

                doThrow(new BadRequestException("usuario.nao.encontrado"))
                                .when(validarUsuarioExistente).execute(usuarioId);

                BadRequestException exception = assertThrows(BadRequestException.class,
                                () -> validarDadosDelecaoEndereco.validarDelecao(enderecoId, usuarioId, restauranteId));
                assertEquals("usuario.nao.encontrado", exception.getMessage());
                verify(validarUsuarioExistente).execute(usuarioId);
                verifyNoInteractions(validarEnderecoExistente, validarProprietarioEndereco);
        }

        @Test
        void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
                Long enderecoId = 1L;
                Long usuarioId = 2L;
                Long restauranteId = null;

                when(validarUsuarioExistente.execute(usuarioId)).thenReturn(null);
                doThrow(new BadRequestException("endereco.nao.encontrado"))
                                .when(validarEnderecoExistente).execute(enderecoId);

                BadRequestException exception = assertThrows(BadRequestException.class,
                                () -> validarDadosDelecaoEndereco.validarDelecao(enderecoId, usuarioId, restauranteId));
                assertEquals("endereco.nao.encontrado", exception.getMessage());
                verify(validarUsuarioExistente).execute(usuarioId);
                verify(validarEnderecoExistente).execute(enderecoId);
                verifyNoInteractions(validarProprietarioEndereco);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoEProprietario() {
                Long enderecoId = 1L;
                Long usuarioId = 2L;
                Long restauranteId = 3L;

                Endereco endereco = new Endereco();
                endereco.setId(enderecoId);

                when(validarUsuarioExistente.execute(usuarioId)).thenReturn(null);
                when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
                doThrow(new BadRequestException("usuario.nao.proprietario"))
                                .when(validarProprietarioEndereco)
                                .validarProprietarioEndereco(endereco, usuarioId, restauranteId);

                BadRequestException exception = assertThrows(BadRequestException.class,
                                () -> validarDadosDelecaoEndereco.validarDelecao(enderecoId, usuarioId, restauranteId));
                assertEquals("usuario.nao.proprietario", exception.getMessage());
                verify(validarUsuarioExistente).execute(usuarioId);
                verify(validarEnderecoExistente).execute(enderecoId);
                verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        }
}
