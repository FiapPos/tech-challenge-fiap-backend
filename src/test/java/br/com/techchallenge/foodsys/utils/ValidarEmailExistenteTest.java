package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarEmailExistenteTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private ValidarEmailExistente validarEmailExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoDuplicado() {
        when(usuarioRepository.existsByEmail("teste@exemplo.com")).thenReturn(false);
        assertDoesNotThrow(() -> validarEmailExistente.execute("teste@exemplo.com"));
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        when(usuarioRepository.existsByEmail("teste@exemplo.com")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> validarEmailExistente.execute("teste@exemplo.com"));
    }
} 