package br.com.techchallenge.foodsys.compartilhado;

import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioLogadoTest {

    private final UsuarioLogado usuarioLogado = new UsuarioLogado();

    @AfterEach
    void limparSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class GetUsuario {
        @Test
        void deveRetornarUsuarioQuandoAutenticadoCorretamente() {
            Usuario usuario = mock(Usuario.class);
            when(usuario.getId()).thenReturn(42L);
            when(usuario.getTipo()).thenReturn(TipoUsuario.CLIENTE);
            DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(usuario);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Usuario> resultado = usuarioLogado.getUsuario();

            assertTrue(resultado.isPresent());
            assertEquals(usuario, resultado.get());
        }

        @Test
        void deveRetornarEmptyQuandoNaoAutenticado() {
            SecurityContextHolder.clearContext();

            Optional<Usuario> resultado = usuarioLogado.getUsuario();
            assertTrue(resultado.isEmpty());
        }

        @Test
        void deveRetornarEmptyQuandoPrincipalNaoForDetalhesUsuarioDto() {
            var authentication = new UsernamePasswordAuthenticationToken("string_estranha", null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Usuario> resultado = usuarioLogado.getUsuario();

            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    class GetUsuarioId {
        @Test
        void deveRetornarIdQuandoIdentificadoCorretamente() {
            Usuario usuario = mock(Usuario.class);
            when(usuario.getId()).thenReturn(99L);
            when(usuario.getTipo()).thenReturn(TipoUsuario.CLIENTE);
            DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(usuario);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Long usuarioId = usuarioLogado.getUsuarioId();

            assertEquals(99L, usuarioId);
        }

        @Test
        void deveRetornarNullQuandoUsuarioNaoEstiverLogado() {
            SecurityContextHolder.clearContext();

            Long usuarioId = usuarioLogado.getUsuarioId();
            assertNull(usuarioId);
        }
    }
}