package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarLoginExistenteTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private ValidarLoginExistente validarLoginExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void naoDeveLancarExcecaoQuandoLoginNaoDuplicado() {
        when(usuarioRepository.existsByLogin("loginTeste")).thenReturn(false);
        assertDoesNotThrow(() -> validarLoginExistente.execute("loginTeste"));
    }

    @Test
    void deveLancarExcecaoQuandoLoginDuplicado() {
        when(usuarioRepository.existsByLogin("loginTeste")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> validarLoginExistente.execute("loginTeste"));
    }
} 