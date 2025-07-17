package br.com.techchallenge.foodsys.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

public class ValidarListaDeEnderecoRestauranteTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;

    private ValidarListaDeEnderecoRestaurante validarListaDeEnderecoRestaurante;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validarListaDeEnderecoRestaurante = new ValidarListaDeEnderecoRestaurante(
                enderecoRepository,
                validarRestauranteExistente);
    }

    @Test
    void deveRetornarListaEnderecosPorRestaurante() {
        Long restauranteId = 2L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(100L);
        endereco1.setRua("Rua Restaurante 1");

        Endereco endereco2 = new Endereco();
        endereco2.setId(200L);
        endereco2.setRua("Rua Restaurante 2");

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);
        when(enderecoRepository.findByRestauranteId(restauranteId)).thenReturn(enderecos);

        List<Endereco> resultado = validarListaDeEnderecoRestaurante.listarPorRestauranteId(restauranteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Rua Restaurante 1", resultado.get(0).getRua());
        assertEquals("Rua Restaurante 2", resultado.get(1).getRua());

        verify(validarRestauranteExistente).execute(restauranteId);
        verify(enderecoRepository).findByRestauranteId(restauranteId);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteIdNulo() {
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarListaDeEnderecoRestaurante.listarPorRestauranteId(null));
        assertEquals("restaurante.nao.informado", exception.getMessage());
        verifyNoInteractions(validarRestauranteExistente, enderecoRepository);
    }
}
