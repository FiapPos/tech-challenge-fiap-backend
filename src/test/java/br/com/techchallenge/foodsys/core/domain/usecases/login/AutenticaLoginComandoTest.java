package br.com.techchallenge.foodsys.core.domain.usecases.login;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.dtos.login.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.CredenciaisInvalidasException;
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

import java.util.HashSet;
import java.util.Set;

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
    void deveFazerLoginComSucesso() throws Exception {
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        // Configurar o tipo de usuário
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(usuarioTipo);
        usuario.setUsuarioTipos(tipos);

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
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
    }
}
