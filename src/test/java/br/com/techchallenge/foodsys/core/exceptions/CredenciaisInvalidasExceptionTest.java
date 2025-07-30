package br.com.techchallenge.foodsys.core.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredenciaisInvalidasExceptionTest {
    @Test
    void deveRetornarMensagemPadrao() {
        CredenciaisInvalidasException exception = new CredenciaisInvalidasException();
        assertEquals("Usuário, senha ou tipo inválidos.", exception.getMessage());
    }
} 