package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarUsuarioExistenteTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private ValidarUsuarioExistente validarUsuarioExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarUsuarioQuandoEncontrado() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        assertEquals(usuario, validarUsuarioExistente.execute(1L));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> validarUsuarioExistente.execute(2L));
    }
} 