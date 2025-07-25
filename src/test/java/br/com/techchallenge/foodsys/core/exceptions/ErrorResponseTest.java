package br.com.techchallenge.foodsys.core.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "erro genérico";
        ErrorResponse errorResponse = new ErrorResponse(mensagem);
        assertEquals(mensagem, errorResponse.getMensagem());
    }
} 