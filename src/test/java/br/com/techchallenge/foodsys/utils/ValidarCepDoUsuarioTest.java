package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarCepDoUsuarioTest {
    @Mock
    private EnderecoRepository enderecoRepository;
    @InjectMocks
    private ValidarCepDoUsuario validarCepDoUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void naoDeveLancarExcecaoQuandoCepNaoDuplicado() {
        when(enderecoRepository.existsByUsuarioIdAndCep(1L, "12345-678")).thenReturn(false);
        assertDoesNotThrow(() -> validarCepDoUsuario.validarCepDuplicado(1L, "12345-678"));
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        when(enderecoRepository.existsByUsuarioIdAndCep(1L, "12345-678")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> validarCepDoUsuario.validarCepDuplicado(1L, "12345-678"));
    }
} 