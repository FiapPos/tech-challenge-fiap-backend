package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.CredenciaisInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AutenticaLoginComandoTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private DetalhesUsuarioDto detalhesUsuarioDto;
    @InjectMocks
    private AutenticaLoginComando autenticaLoginComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveFazerLoginComSucesso() {
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha);
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(detalhesUsuarioDto);
        when(detalhesUsuarioDto.usuario()).thenReturn(usuario);

        Usuario resultado = autenticaLoginComando.login(credenciais);
        assertEquals(usuario, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        String login = "usuario1";
        String senha = "senhaIncorreta";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
    }
} 