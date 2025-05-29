package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.compartilhado.UsuarioLogado;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizaCredenciaisComandoTest {

    @Mock
    private UsuarioLogado usuarioLogado;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AtualizaCredenciaisComando comando;

    @Nested
    class Execute {
        @Test
        void deve_atualizar_senha_quando_dados_forem_validos() {
            Long usuarioId = 1L;
            var dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");

            Usuario usuario = mock(Usuario.class);
            when(usuarioLogado.getUsuarioId()).thenReturn(usuarioId);
            when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.encode("novaSenha123")).thenReturn("senhaCodificada");

            comando.execute(dto);
            verify(usuario).trocaSenha("senhaCodificada");
        }

        @Test
        void deve_lancar_excecao_quando_senha_for_nula() {
            var dto = new AtualizaCredenciaisComandoDto(null, "abc12345");

            var ex = assertThrows(IllegalArgumentException.class, () -> comando.execute(dto));
            assertTrue(ex.getMessage().contains("A senha não pode ser nula"));
        }

        @Test
        void deve_lancar_excecao_quando_confirmacao_for_nula() {
            var dto = new AtualizaCredenciaisComandoDto("abc12345", null);

            var ex = assertThrows(IllegalArgumentException.class, () -> comando.execute(dto));
            assertTrue(ex.getMessage().contains("A confirmação da senha não pode ser nula"));
        }

        @Test
        void deve_lancar_excecao_quando_usuario_nao_estiver_logado() {
            when(usuarioLogado.getUsuarioId()).thenReturn(null);
            var dto = new AtualizaCredenciaisComandoDto("abc12345", "abc12345");

            var ex = assertThrows(IllegalArgumentException.class, () -> comando.execute(dto));
            assertTrue(ex.getMessage().contains("A pessoa usuária precisa estar logada"));
        }

        @Test
        void nao_deve_fazer_nada_quando_usuario_nao_for_encontrado() {
            Long usuarioId = 999L;
            var dto = new AtualizaCredenciaisComandoDto("abc12345", "abc12345");

            when(usuarioLogado.getUsuarioId()).thenReturn(usuarioId);
            when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> comando.execute(dto));
            verify(usuarioRepository).findById(usuarioId);
            verifyNoMoreInteractions(passwordEncoder);
        }
    }
}