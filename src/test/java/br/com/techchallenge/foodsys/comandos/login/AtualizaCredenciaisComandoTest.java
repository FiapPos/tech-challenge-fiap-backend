package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.compartilhado.UsuarioLogado;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizaCredenciaisComandoTest {
    @Mock
    private UsuarioLogado usuarioLogado;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private AtualizaCredenciaisComando atualizaCredenciaisComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAtualizarCredenciaisComSucesso() {
        Long usuarioId = 1L;
        String novaSenha = "novaSenha123";
        String senhaCriptografada = "senhaCriptografada";
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto(novaSenha, novaSenha);
        Usuario usuario = mock(Usuario.class);
        when(usuarioLogado.getUsuarioId()).thenReturn(usuarioId);
        when(passwordEncoder.encode(novaSenha)).thenReturn(senhaCriptografada);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        atualizaCredenciaisComando.execute(dto);
        verify(usuario).trocaSenha(senhaCriptografada);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForNula() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto(null, "confirmacao");
        assertThrows(IllegalArgumentException.class, () -> atualizaCredenciaisComando.execute(dto));
    }

    @Test
    void deveLancarExcecaoQuandoConfirmacaoSenhaForNula() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("senha", null);
        assertThrows(IllegalArgumentException.class, () -> atualizaCredenciaisComando.execute(dto));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEstiverLogado() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("senha", "senha");
        when(usuarioLogado.getUsuarioId()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> atualizaCredenciaisComando.execute(dto));
    }
}