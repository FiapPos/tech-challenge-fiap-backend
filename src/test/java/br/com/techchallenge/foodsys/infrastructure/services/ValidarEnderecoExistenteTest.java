package br.com.techchallenge.foodsys.infrastructure.services;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarEnderecoExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ValidarEnderecoExistenteTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @InjectMocks
    private ValidarEnderecoExistente validarEnderecoExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarEnderecoQuandoEncontrado() {
        Endereco endereco = new Endereco();
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        assertEquals(endereco, validarEnderecoExistente.execute(1L));
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> validarEnderecoExistente.execute(2L));
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoRestauranteExiste() {
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        when(enderecoRepository.existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId));
        assertEquals("endereco.restaurante.ja.cadastrado", exception.getMessage());
        verify(enderecoRepository, times(1)).existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId);
    }

    @Test
    void deveRetornarFalseQuandoRestauranteNulo() {
        Long usuarioId = 1L;
        Long restauranteId = null;

        boolean resultado = validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId);

        assertFalse(resultado);
        verify(enderecoRepository, never()).existsByUsuarioIdAndRestauranteId(any(), any());
    }

    @Test
    void deveRetornarFalseQuandoEnderecoRestauranteNaoExiste() {
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        when(enderecoRepository.existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId)).thenReturn(false);

        boolean resultado = validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId);

        assertFalse(resultado);
        verify(enderecoRepository, times(1)).existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId);
    }
}