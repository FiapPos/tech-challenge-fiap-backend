package br.com.techchallenge.foodsys.core.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemDoCardapioNaoEncontradoExceptionTest {

    @Test
    void deveCriarExcecaoComMensagemCorreta() {
        // Arrange
        Long id = 1L;

        // Act
        ItemDoCardapioNaoEncontradoException exception = new ItemDoCardapioNaoEncontradoException(id);

        // Assert
        assertNotNull(exception);
        assertEquals("Item do cardápio não encontrado com id: 1", exception.getMessage());
    }

    @Test
    void deveCriarExcecaoComIdDiferente() {
        // Arrange
        Long id = 999L;

        // Act
        ItemDoCardapioNaoEncontradoException exception = new ItemDoCardapioNaoEncontradoException(id);

        // Assert
        assertNotNull(exception);
        assertEquals("Item do cardápio não encontrado com id: 999", exception.getMessage());
    }

    @Test
    void deveSerInstanciaDeRuntimeException() {
        // Arrange
        Long id = 1L;

        // Act
        ItemDoCardapioNaoEncontradoException exception = new ItemDoCardapioNaoEncontradoException(id);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void deveCriarExcecaoComIdZero() {
        // Arrange
        Long id = 0L;

        // Act
        ItemDoCardapioNaoEncontradoException exception = new ItemDoCardapioNaoEncontradoException(id);

        // Assert
        assertNotNull(exception);
        assertEquals("Item do cardápio não encontrado com id: 0", exception.getMessage());
    }
} 