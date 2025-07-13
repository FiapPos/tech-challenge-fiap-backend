package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

class FiltroAutenticacaoJwtTest {
    @Mock
    private AutenticaJwtComando jwtService;
    @Mock
    private AutenticaUsuarioComando userAuthenticationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private FiltroAutenticacaoJwt filtroAutenticacaoJwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void deveProcessarRequisicaoSemHeaderDeAutorizacao() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filtroAutenticacaoJwt.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userAuthenticationService);
    }

    @Test
    void deveProcessarRequisicaoComHeaderBearerVazio() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");

        filtroAutenticacaoJwt.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userAuthenticationService);
    }

    @Test
    void deveAutenticarComTokenValido() throws ServletException, IOException {
        String token = "validToken";
        String login = "usuario1";
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.getLogin(token)).thenReturn(login);
        when(userAuthenticationService.getByLogin(login)).thenReturn(usuario);

        filtroAutenticacaoJwt.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).getLogin(token);
        verify(userAuthenticationService).getByLogin(login);
    }

    @Test
    void deveContinuarQuandoTokenInvalido() throws ServletException, IOException {
        String token = "invalidToken";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.getLogin(token)).thenThrow(new RuntimeException("Invalid token"));

        filtroAutenticacaoJwt.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).getLogin(token);
        verifyNoInteractions(userAuthenticationService);
    }

    @Test
    void deveContinuarQuandoHeaderNaoComecaComBearer() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic dXNlcjpwYXNz");

        filtroAutenticacaoJwt.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userAuthenticationService);
    }
} 