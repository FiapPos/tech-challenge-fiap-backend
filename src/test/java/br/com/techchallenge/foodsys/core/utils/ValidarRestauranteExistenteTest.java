package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.utils.restaurante.ValidarRestauranteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarRestauranteExistenteTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    private ValidarRestauranteExistente validarRestauranteExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validarRestauranteExistente = new ValidarRestauranteExistente(restauranteRepository);
    }

    @Test
    void deveRetornarRestauranteQuandoEncontrado() {
        Long id = 1L;
        Restaurante restaurante = new Restaurante();
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));

        Restaurante resultado = validarRestauranteExistente.execute(id);

        assertNotNull(resultado);
        assertEquals(restaurante, resultado);
        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        Long id = 2L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarRestauranteExistente.execute(id));
        assertEquals("restaurante.nao.encontrado", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicado() {
        String nome = "Restaurante Teste";
        Restaurante restaurante = new Restaurante();
        when(restauranteRepository.findRestauranteByNome(nome)).thenReturn(restaurante);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarRestauranteExistente.validarNomeRestauranteDuplicado(nome));
        assertEquals("restaurante.duplicado", exception.getMessage());
        verify(restauranteRepository, times(1)).findRestauranteByNome(nome);
    }

    @Test
    void naoDeveLancarExcecaoQuandoNomeNaoForDuplicado() {
        String nome = "Restaurante Teste";
        when(restauranteRepository.findRestauranteByNome(nome)).thenReturn(null);

        assertDoesNotThrow(() -> validarRestauranteExistente.validarNomeRestauranteDuplicado(nome));
        verify(restauranteRepository, times(1)).findRestauranteByNome(nome);
    }
}