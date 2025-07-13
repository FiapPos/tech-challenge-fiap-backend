package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.login.AtualizaCredenciaisComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidaConfirmacaoDeSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;

import java.util.HashMap;
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
    void deveFazerLoginComSucesso() {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123");
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        String token = "jwtToken";
        
        when(bindingResult.hasErrors()).thenReturn(false);
        when(autenticaLoginComando.login(credenciais)).thenReturn(usuario);
        when(autenticaJwtComando.createToken(usuario)).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().get("token"));
    }

    @Test
    void deveRetornarErroQuandoCredenciaisInvalidas() {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123");
        
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(
            new FieldError("credentials", "login", "Login é obrigatório")
        ));

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Login é obrigatório", response.getBody().get("login"));
    }

    @Test
    void deveAtualizarSenhaComSucesso() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");
        
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(atualizaCredenciaisComando).execute(dto);

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        verify(atualizaCredenciaisComando).execute(dto);
    }

    @Test
    void deveRetornarErroQuandoAtualizacaoSenhaInvalida() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("senha", "senhaDiferente");
        
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(
            new FieldError("dto", "confirmacaoSenha", "Senhas não conferem")
        ));

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Senhas não conferem", response.getBody().get("confirmacaoSenha"));
    }

    @Test
    void deveConfigurarValidadorNoInitBinder() {
        WebDataBinder webDataBinder = mock(WebDataBinder.class);
        
        loginController.initBinder(webDataBinder);
        
        verify(webDataBinder).addValidators(validaConfirmacaoDeSenha);
    }

    @Test
    void deveRetornarTokenQuandoLoginBemSucedido() {
        CredenciaisUsuarioDto credenciais = new CredenciaisUsuarioDto("usuario1", "senha123");
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");
        String token = "jwtToken";
        
        when(bindingResult.hasErrors()).thenReturn(false);
        when(autenticaLoginComando.login(credenciais)).thenReturn(usuario);
        when(autenticaJwtComando.createToken(usuario)).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(credenciais, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().get("token"));
        verify(autenticaLoginComando).login(credenciais);
        verify(autenticaJwtComando).createToken(usuario);
    }

    @Test
    void deveRetornarOkQuandoAtualizacaoSenhaBemSucedida() {
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");
        
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(atualizaCredenciaisComando).execute(dto);

        ResponseEntity<Map<String, String>> response = loginController.atualizaSenha(dto, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(atualizaCredenciaisComando).execute(dto);
    }
} 