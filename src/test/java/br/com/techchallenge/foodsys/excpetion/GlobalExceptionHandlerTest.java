package br.com.techchallenge.foodsys.excpetion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveTratarBadRequestException() {
        String mensagemKey = "erro.badrequest";
        String mensagemTraduzida = "Mensagem traduzida";
        Locale currentLocale = LocaleContextHolder.getLocale();
        when(messageSource.getMessage(eq(mensagemKey), eq(null), eq(currentLocale))).thenReturn(mensagemTraduzida);
        BadRequestException ex = new BadRequestException(mensagemKey);

        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagemTraduzida, response.getBody().getMensagem());
    }

    @Test
    void deveTratarCredenciaisInvalidasException() {
        CredenciaisInvalidasException ex = new CredenciaisInvalidasException();
        ResponseEntity<?> response = handler.handleCredenciaisInvalidas(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Usuário ou senha inválidos.", body.get("erro"));
    }

    @Test
    void deveTratarForbiddenException() {
        String mensagemKey = "erro.forbidden";
        String mensagemTraduzida = "Acesso negado";
        Locale currentLocale = LocaleContextHolder.getLocale();
        when(messageSource.getMessage(eq(mensagemKey), eq(null), eq(currentLocale))).thenReturn(mensagemTraduzida);
        ForbiddenException ex = new ForbiddenException(mensagemKey);

        ResponseEntity<ErrorResponse> response = handler.handleForbiddenException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(mensagemTraduzida, response.getBody().getMensagem());
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemTraduzida() {
        String fieldName = "email";
        String messageKey = "{validation.email.invalid}";
        String translatedMessage = "Email inválido";
        
        FieldError fieldError = new FieldError("object", fieldName, messageKey);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError));
        
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        Locale currentLocale = LocaleContextHolder.getLocale();
        when(messageSource.getMessage(eq("validation.email.invalid"), eq(null), eq("validation.email.invalid"), eq(currentLocale)))
                .thenReturn(translatedMessage);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals(translatedMessage, errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemPadrao() {
        String fieldName = "nome";
        String defaultMessage = "Nome é obrigatório";
        
        FieldError fieldError = new FieldError("object", fieldName, defaultMessage);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError));
        
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals(defaultMessage, errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemNula() {
        String fieldName = "idade";
        String defaultMessage = null;
        
        FieldError fieldError = new FieldError("object", fieldName, defaultMessage);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError));
        
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertNull(errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMultiplosErros() {
        FieldError error1 = new FieldError("object", "email", "{validation.email.invalid}");
        FieldError error2 = new FieldError("object", "senha", "Senha é obrigatória");
        
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(error1, error2));
        
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        Locale currentLocale = LocaleContextHolder.getLocale();
        when(messageSource.getMessage(eq("validation.email.invalid"), eq(null), eq("validation.email.invalid"), eq(currentLocale)))
                .thenReturn("Email inválido");

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals("Email inválido", errors.get("email"));
        assertEquals("Senha é obrigatória", errors.get("senha"));
        assertEquals(2, errors.size());
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemQueComecaComChaveMasNaoTermina() {
        String fieldName = "telefone";
        String defaultMessage = "{validation.phone.invalid mas sem fechar";
        
        FieldError fieldError = new FieldError("object", fieldName, defaultMessage);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError));
        
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals(defaultMessage, errors.get(fieldName));
    }
} 