package br.com.techchallenge.foodsys.excpetion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenExceptionTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "acesso negado";
        ForbiddenException exception = new ForbiddenException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
} 