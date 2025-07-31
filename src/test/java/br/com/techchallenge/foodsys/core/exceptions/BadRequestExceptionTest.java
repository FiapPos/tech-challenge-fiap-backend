package br.com.techchallenge.foodsys.core.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "mensagem de erro";
        BadRequestException exception = new BadRequestException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
} 