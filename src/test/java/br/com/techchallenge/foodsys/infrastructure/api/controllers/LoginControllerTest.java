package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.usecases.login.AtualizaCredenciaisComando;
import br.com.techchallenge.foodsys.core.domain.usecases.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.core.domain.usecases.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.core.dtos.login.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.core.dtos.login.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.utils.ValidaConfirmacaoDeSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {
    @Mock
    private AutenticaJwtComando autenticaJwtComando;
    @Mock
    private AutenticaLoginComando autenticaLoginComando;
    @Mock
    private AtualizaCredenciaisComando atualizaCredenciaisComando;
    @Mock
    private ValidaConfirmacaoDeSenha validaConfirmacaoDeSenha;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123", null);
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        String token = "jwtToken";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(autenticaLoginComando.login(credenciais)).thenReturn(usuario);
        when(autenticaJwtComando.createToken(usuario, null)).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().get("token"));
    }

    @Test
    void deveRetornarErroQuandoCredenciaisInvalidas() throws Exception {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123", null);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(
                new FieldError("credentials", "login", "Login é obrigatório")));

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login é obrigatório", response.getBody().get("login"));
    }

    @Test
    void deveAtualizarSenhaComSucesso() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(atualizaCredenciaisComando).execute(dto);

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizaCredenciaisComando).execute(dto);
    }

    @Test
    void deveRetornarErroQuandoAtualizacaoSenhaInvalida() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("senha", "senhaDiferente");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(
                new FieldError("dto", "confirmacaoSenha", "Senhas não conferem")));

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Senhas não conferem", response.getBody().get("confirmacaoSenha"));
    }

    @Test
    void deveConfigurarValidadorNoInitBinder() {
        WebDataBinder webDataBinder = mock(WebDataBinder.class);

        loginController.initBinder(webDataBinder);

        verify(webDataBinder).addValidators(validaConfirmacaoDeSenha);
    }

    @Test
    void deveRetornarTokenQuandoLoginBemSucedido() throws Exception {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123", null);
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        String token = "jwtToken";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(autenticaLoginComando.login(credenciais)).thenReturn(usuario);
        when(autenticaJwtComando.createToken(usuario, null)).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().get("token"));
        verify(autenticaLoginComando).login(credenciais);
        verify(autenticaJwtComando).createToken(usuario, null);
    }

    @Test
    void deveRetornarOkQuandoAtualizacaoSenhaBemSucedida() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(atualizaCredenciaisComando).execute(dto);

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizaCredenciaisComando).execute(dto);
    }
}