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

public class ValidarCepDuplicadoTest {
    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ValidarCepDuplicado validarCepDuplicado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicadoParaRestaurante() {
        Long usuarioId = null;
        Long restauranteId = 1L;
        String cep = "12345678";

        when(enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarCepDuplicado.validarCep(usuarioId, restauranteId, cep));
        assertEquals("cep.ja.cadastrado.para.restaurante", exception.getMessage());
        verify(enderecoRepository, times(1)).existsByRestauranteIdAndCep(restauranteId, cep);
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicadoParaUsuario() {
        Long usuarioId = 1L;
        Long restauranteId = null;
        String cep = "12345678";

        when(enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarCepDuplicado.validarCep(usuarioId, restauranteId, cep));
        assertEquals("cep.ja.cadastrado.para.usuario", exception.getMessage());
        verify(enderecoRepository, times(1)).existsByUsuarioIdAndCep(usuarioId, cep);
    }

    @Test
    void naoDeveLancarExcecaoQuandoCepDuplicadoParaRestaurante() {
        Long usuarioId = null;
        Long restauranteId = 1L;
        String cep = "12345678";

        when(enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep)).thenReturn(false);

        assertDoesNotThrow(() -> validarCepDuplicado.validarCep(usuarioId, restauranteId, cep));
        verify(enderecoRepository, times(1)).existsByRestauranteIdAndCep(restauranteId, cep);
    }

    @Test
    void naoDeveLancarExcecaoQuandoCepDuplicadoParaUsuario() {
        Long usuarioId = 1L;
        Long restauranteId = null;
        String cep = "12345678";

        when(enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep)).thenReturn(false);

        assertDoesNotThrow(() -> validarCepDuplicado.validarCep(usuarioId, restauranteId, cep));
        verify(enderecoRepository, times(1)).existsByUsuarioIdAndCep(usuarioId, cep);
    }
}
