package br.com.techchallenge.foodsys.excpetion;

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