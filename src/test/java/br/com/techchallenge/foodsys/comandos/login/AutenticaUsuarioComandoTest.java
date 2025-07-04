package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutenticaUsuarioComandoTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private AutenticaUsuarioComando autenticaUsuarioComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarUsuarioQuandoEncontrado() {
        String login = "usuario1";
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.of(usuario));

        Usuario resultado = autenticaUsuarioComando.getByLogin(login);
        assertEquals(usuario, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        String login = "usuarioInexistente";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> autenticaUsuarioComando.getByLogin(login));
    }
} 