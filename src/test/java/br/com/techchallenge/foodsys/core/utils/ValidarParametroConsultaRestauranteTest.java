package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.queries.params.ListarRestaurantesParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarParametroConsultaRestauranteTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ValidarParametroConsultaRestaurante validarParametroConsultaRestaurante;

    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private List<Restaurante> restaurantes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante 1");
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setAtivo(true);
        
        restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setTipoCozinha("Japonesa");
        restaurante2.setAtivo(false);
        
        restaurantes = Arrays.asList(restaurante1, restaurante2);
    }

    @Test
    void deveBuscarPorAtivoETipoCozinha() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);
        params.setTipoCozinha("Italiana");
        
        when(restauranteRepository.findByAtivoAndTipoCozinha(true, "Italiana"))
            .thenReturn(Arrays.asList(restaurante1));

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Italiana", resultado.get(0).getTipoCozinha());
        assertTrue(resultado.get(0).isAtivo());
        
        verify(restauranteRepository).findByAtivoAndTipoCozinha(true, "Italiana");
    }

    @Test
    void deveBuscarApenasPorTipoCozinha() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setTipoCozinha("Japonesa");
        
        when(restauranteRepository.findByTipoCozinha("Japonesa"))
            .thenReturn(Arrays.asList(restaurante2));

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Japonesa", resultado.get(0).getTipoCozinha());
        
        verify(restauranteRepository).findByTipoCozinha("Japonesa");
    }

    @Test
    void deveBuscarApenasPorAtivo() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(false);
        
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(restauranteRepository.findByAtivo(false, sort))
            .thenReturn(Arrays.asList(restaurante2));

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isAtivo());
        
        verify(restauranteRepository).findByAtivo(false, sort);
    }

    @Test
    void deveBuscarTodosQuandoNenhumParametro() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(restauranteRepository.findAll(sort))
            .thenReturn(restaurantes);

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        verify(restauranteRepository).findAll(sort);
    }

    @Test
    void deveBuscarTodosQuandoTipoCozinhaVazio() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);
        params.setTipoCozinha("");
        
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(restauranteRepository.findByAtivo(true, sort))
            .thenReturn(Arrays.asList(restaurante1));

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(restauranteRepository).findByAtivo(true, sort);
    }

    @Test
    void deveBuscarTodosQuandoTipoCozinhaNull() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);
        params.setTipoCozinha(null);
        
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(restauranteRepository.findByAtivo(true, sort))
            .thenReturn(Arrays.asList(restaurante1));

        // Act
        List<Restaurante> resultado = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(restauranteRepository).findByAtivo(true, sort);
    }
}
