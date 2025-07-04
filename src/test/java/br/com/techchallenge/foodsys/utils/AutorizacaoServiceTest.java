package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorizacaoServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private AutorizacaoService autorizacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void deveRetornarAutenticacaoAtualQuandoAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        assertEquals(authentication, autorizacaoService.getAutenticacaoAtual());
    }

    @Test
    void deveLancarForbiddenExceptionQuandoNaoAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(null);
        assertThrows(ForbiddenException.class, () -> autorizacaoService.getAutenticacaoAtual());
    }

    @Test
    void deveLancarForbiddenExceptionQuandoAuthenticationNaoAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(ForbiddenException.class, () -> autorizacaoService.getAutenticacaoAtual());
    }

    @Test
    void deveExtrairUsernameDeUserDetails() {
        when(userDetails.getUsername()).thenReturn("usuario1");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        assertEquals("usuario1", autorizacaoService.extrairUsernameAutenticacao(authentication));
    }

    @Test
    void deveExtrairUsernameDePrincipalString() {
        when(authentication.getPrincipal()).thenReturn("usuario2");
        assertEquals("usuario2", autorizacaoService.extrairUsernameAutenticacao(authentication));
    }

    @Test
    void deveBuscarUsuarioPorUsername() {
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        when(usuarioRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuario));
        assertEquals(usuario, autorizacaoService.buscarUsuarioPorUsername("usuario1"));
    }

    @Test
    void deveLancarForbiddenExceptionQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByLogin("usuarioX")).thenReturn(Optional.empty());
        assertThrows(ForbiddenException.class, () -> autorizacaoService.buscarUsuarioPorUsername("usuarioX"));
    }

    @Test
    void deveRetornarUsuarioLogado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("usuario1");
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        when(usuarioRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuario));
        assertEquals(usuario, autorizacaoService.getUsuarioLogado());
    }

    @Test
    void deveRetornarTrueQuandoUsuarioAutorizado() {
        assertTrue(autorizacaoService.isUsuarioAutorizado(1L, 1L));
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoAutorizado() {
        assertFalse(autorizacaoService.isUsuarioAutorizado(1L, 2L));
    }

    @Test
    void deveValidarAcessoUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("usuario1");
        when(usuarioRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuario));
        assertDoesNotThrow(() -> autorizacaoService.validarAcessoUsuario(1L));
    }

    @Test
    void deveLancarForbiddenExceptionQuandoAcessoNaoAutorizado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("usuario1");
        when(usuarioRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuario));
        assertThrows(ForbiddenException.class, () -> autorizacaoService.validarAcessoUsuario(2L));
    }
} 