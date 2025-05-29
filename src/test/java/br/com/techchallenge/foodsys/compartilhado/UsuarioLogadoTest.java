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
        void deve_retornar_usuario_quando_autenticado_corretamente() {
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
        void deve_retornar_empty_quando_nao_autenticado() {
            SecurityContextHolder.clearContext();

            Optional<Usuario> resultado = usuarioLogado.getUsuario();
            assertTrue(resultado.isEmpty());
        }

        @Test
        void deve_retornar_empty_quando_principal_nao_for_detalhes_usuario_dto() {
            var authentication = new UsernamePasswordAuthenticationToken("string_estranha", null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Usuario> resultado = usuarioLogado.getUsuario();

            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    class GetUsuarioId {
        @Test
        void deve_retornar_id_quando_identificado_corretamente() {
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
        void deve_retornar_null_quando_usuario_nao_estiver_logado() {
            SecurityContextHolder.clearContext();

            Long usuarioId = usuarioLogado.getUsuarioId();
            assertNull(usuarioId);
        }
    }
}