package br.com.techchallenge.foodsys.compartilhado;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CompartilhadoServiceTest {

    private CompartilhadoService compartilhadoService;

    @BeforeEach
    void setUp() {
        compartilhadoService = new CompartilhadoService();
    }

    @Test
    void deveRetornarDataHoraAtual() {
        LocalDateTime antes = LocalDateTime.now();
        LocalDateTime resultado = compartilhadoService.getCurrentDateTime();
        LocalDateTime depois = LocalDateTime.now();

        assertNotNull(resultado);
        assertTrue(resultado.isAfter(antes) || resultado.isEqual(antes));
        assertTrue(resultado.isBefore(depois) || resultado.isEqual(depois));
    }

    @Test
    void deveRetornarDataHoraDiferentesEmChamadasSeparadas() {
        LocalDateTime primeira = compartilhadoService.getCurrentDateTime();
        
        // Simular um pequeno delay
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LocalDateTime segunda = compartilhadoService.getCurrentDateTime();

        assertTrue(segunda.isAfter(primeira));
    }
} 