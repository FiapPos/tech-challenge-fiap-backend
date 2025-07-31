package br.com.techchallenge.foodsys.core.domain.usecases.login;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.dtos.login.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.exceptions.CredenciaisInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Arrange
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

        // Act
        Usuario resultado = autenticaLoginComando.login(credenciais);
        
        // Assert
        assertEquals(usuario, resultado);
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
        verify(detalhesUsuarioDto).usuario();
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        // Arrange
        String login = "usuario1";
        String senha = "senhaIncorreta";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipoSolicitado() throws Exception {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.ADMIN);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        // Configurar o tipo de usuário como CLIENTE, mas solicitar ADMIN
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(usuarioTipo);
        usuario.setUsuarioTipos(tipos);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(detalhesUsuarioDto);
        when(detalhesUsuarioDto.usuario()).thenReturn(usuario);

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
        verify(detalhesUsuarioDto).usuario();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipos() throws Exception {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setUsuarioTipos(new HashSet<>()); // Usuário sem tipos

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(detalhesUsuarioDto);
        when(detalhesUsuarioDto.usuario()).thenReturn(usuario);

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
        verify(detalhesUsuarioDto).usuario();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioTemMultiplosTiposMasNaoORequerido() throws Exception {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.ADMIN);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        // Configurar múltiplos tipos, mas não o ADMIN
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        tipoCliente.setUsuario(usuario);

        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        tipoDono.setUsuario(usuario);

        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(tipoCliente);
        tipos.add(tipoDono);
        usuario.setUsuarioTipos(tipos);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(detalhesUsuarioDto);
        when(detalhesUsuarioDto.usuario()).thenReturn(usuario);

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
        verify(detalhesUsuarioDto).usuario();
    }

    @Test
    void deveFazerLoginComSucessoQuandoUsuarioTemMultiplosTiposIncluindoORequerido() throws Exception {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.ADMIN);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        // Configurar múltiplos tipos, incluindo ADMIN
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        tipoCliente.setUsuario(usuario);

        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        tipoAdmin.setUsuario(usuario);

        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(tipoCliente);
        tipos.add(tipoAdmin);
        usuario.setUsuarioTipos(tipos);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(detalhesUsuarioDto);
        when(detalhesUsuarioDto.usuario()).thenReturn(usuario);

        // Act
        Usuario resultado = autenticaLoginComando.login(credenciais);
        
        // Assert
        assertEquals(usuario, resultado);
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
        verify(detalhesUsuarioDto).usuario();
    }

    @Test
    void deveLancarBadRequestExceptionQuandoExcecaoOriginalEraBadRequestException() {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);
        when(authenticationManager.authenticate(any())).thenThrow(new BadRequestException("Bad request"));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void deveLancarCredenciaisInvalidasExceptionQuandoExcecaoGenerica() {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Generic exception"));

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void deveLancarCredenciaisInvalidasExceptionQuandoExcecaoNoExtracaoUsuario() throws Exception {
        // Arrange
        String login = "usuario1";
        String senha = "senha123";
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto(login, senha, TipoUsuario.CLIENTE);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenThrow(new RuntimeException("Error extracting user"));

        // Act & Assert
        assertThrows(CredenciaisInvalidasException.class, () -> autenticaLoginComando.login(credenciais));
        
        // Verify
        verify(authenticationManager).authenticate(any());
        verify(authentication).getPrincipal();
    }
}
