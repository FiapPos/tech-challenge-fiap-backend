package br.com.techchallenge.foodsys.infrastructure.services;

import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarCepRestauranteDuplicado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidarCepRestauranteDuplicadoTest {
    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ValidarCepRestauranteDuplicado validarCepRestauranteDuplicado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoCepJaCadastradoParaRestaurante() {
        Long restauranteId = 1L;
        String cep = "12345-000";

        when(enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep)).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarCepRestauranteDuplicado.validarCep(restauranteId, cep));
        assertEquals("cep.ja.cadastrado.para.restaurante", exception.getMessage());
        verify(enderecoRepository).existsByRestauranteIdAndCep(restauranteId, cep);
    }

    @Test
    void naoDeveLancarExcecaoQuandoCepNaoCadastradoParaRestaurante() {
        Long restauranteId = 1L;
        String cep = "12345-000";

        when(enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep)).thenReturn(false);

        assertDoesNotThrow(() -> validarCepRestauranteDuplicado.validarCep(restauranteId, cep));
        verify(enderecoRepository).existsByRestauranteIdAndCep(restauranteId, cep);
    }
}
