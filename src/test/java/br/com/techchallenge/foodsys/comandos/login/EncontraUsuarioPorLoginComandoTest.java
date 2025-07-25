package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EncontraUsuarioPorLoginComandoTest {
    @Mock
    private AutenticaUsuarioComando userAuthenticationService;
    @InjectMocks
    private EncontraUsuarioPorLoginComando encontraUsuarioPorLoginComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarUserDetailsQuandoUsuarioEncontrado() {
        String username = "usuario1";
        Usuario usuario = new Usuario();
        usuario.setLogin(username);
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);
        when(userAuthenticationService.getByLogin(username)).thenReturn(usuario);

        UserDetails userDetails = encontraUsuarioPorLoginComando.loadUserByUsername(username);
        assertInstanceOf(DetalhesUsuarioDto.class, userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        String username = "usuarioInexistente";
        when(userAuthenticationService.getByLogin(username)).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> encontraUsuarioPorLoginComando.loadUserByUsername(username));
    }
} 