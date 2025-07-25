package br.com.techchallenge.foodsys.comandos.restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.DesativarRestauranteComando;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.utils.restaurante.ValidarRestauranteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

public class DesativarRestauranteComandoTest {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private CompartilhadoService sharedService;
    @InjectMocks
    private DesativarRestauranteComando comando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveDesativarRestauranteComSucesso() {

        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setAtivo(true);
        LocalDateTime dataDesativacao = LocalDateTime.now();

        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);
        when(sharedService.getCurrentDateTime()).thenReturn(dataDesativacao);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = comando.execute(restauranteId);

        assertFalse(resultado.isAtivo());
        assertEquals(dataDesativacao, resultado.getDataDesativacao());
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteJaDesativado() {
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setAtivo(false);

        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            comando.execute(restauranteId);
        });

        assertEquals("restaurante.ja.esta.desativado", exception.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

}
