package br.com.techchallenge.foodsys.excpetion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredenciaisInvalidasExceptionTest {
    @Test
    void deveRetornarMensagemPadrao() {
        CredenciaisInvalidasException exception = new CredenciaisInvalidasException();
        assertEquals("Usuário ou senha inválidos.", exception.getMessage());
    }
} 