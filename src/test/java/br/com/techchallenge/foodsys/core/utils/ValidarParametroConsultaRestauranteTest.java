package br.com.techchallenge.foodsys.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.queries.restaurante.ListarRestaurantesQuery;
import br.com.techchallenge.foodsys.infrastructure.api.controllers.RestauranteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

public class ValidarParametroConsultaRestauranteTest {

    @Mock
    private ListarRestaurantesQuery listarRestaurantesQuery;
    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private RestauranteController restauranteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarListaDeRestaurantePorTipoCozinha() {
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setTipoCozinha("Italiana");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano 1");
        restaurante1.setAtivo(true);
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setHorarioAbertura("08:00");
        restaurante1.setHorarioFechamento("22:00");
        restaurante1.setUsuarioDonoId(10L);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Italiano 2");
        restaurante2.setAtivo(true);
        restaurante2.setTipoCozinha("Italiana");
        restaurante2.setHorarioAbertura("09:00");
        restaurante2.setHorarioFechamento("23:00");
        restaurante2.setUsuarioDonoId(20L);

        List<Restaurante> listaTipoCozinha = List.of(restaurante1, restaurante2);

        when(restauranteRepository.findByTipoCozinha("Italiana")).thenReturn(listaTipoCozinha);

        ValidarParametroConsultaRestaurante validarListaTipoCozinha = new ValidarParametroConsultaRestaurante(
                restauranteRepository);

        List<Restaurante> resultadoTipoCozinha = validarListaTipoCozinha.validarParametrosConsultaRestaurante(params);

        assertNotNull(resultadoTipoCozinha);
        assertEquals(2, resultadoTipoCozinha.size());
        assertEquals("Restaurante Italiano 1", resultadoTipoCozinha.get(0).getNome());
        assertEquals("Restaurante Italiano 2", resultadoTipoCozinha.get(1).getNome());
        assertEquals("Italiana", resultadoTipoCozinha.get(0).getTipoCozinha());
        assertEquals("Italiana", resultadoTipoCozinha.get(1).getTipoCozinha());
        assertTrue(resultadoTipoCozinha.get(0).isAtivo());
        assertTrue(resultadoTipoCozinha.get(1).isAtivo());
        assertEquals("08:00", resultadoTipoCozinha.get(0).getHorarioAbertura());
        assertEquals("22:00", resultadoTipoCozinha.get(0).getHorarioFechamento());
        assertEquals("09:00", resultadoTipoCozinha.get(1).getHorarioAbertura());
        assertEquals("23:00", resultadoTipoCozinha.get(1).getHorarioFechamento());
        assertEquals(10L, resultadoTipoCozinha.get(0).getUsuarioDonoId());
        assertEquals(20L, resultadoTipoCozinha.get(1).getUsuarioDonoId());
        verify(restauranteRepository, times(1)).findByTipoCozinha("Italiana");
    }

    @Test
    void deveRetornarListaDeRestauranteQuandoForAtivo() {
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano 1");
        restaurante1.setAtivo(true);
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setHorarioAbertura("08:00");
        restaurante1.setHorarioFechamento("22:00");
        restaurante1.setUsuarioDonoId(10L);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Italiano 2");
        restaurante2.setAtivo(true);
        restaurante2.setTipoCozinha("Italiana");
        restaurante2.setHorarioAbertura("09:00");
        restaurante2.setHorarioFechamento("23:00");
        restaurante2.setUsuarioDonoId(20L);

        List<Restaurante> listaAtiva = List.of(restaurante1, restaurante2);

        when(restauranteRepository.findByAtivo(true, Sort.by(Sort.Direction.ASC, "id"))).thenReturn(listaAtiva);

        ValidarParametroConsultaRestaurante validarAtivo = new ValidarParametroConsultaRestaurante(
                restauranteRepository);

        List<Restaurante> resultadoAtivo = validarAtivo.validarParametrosConsultaRestaurante(params);
        assertNotNull(resultadoAtivo);
        assertEquals(2, resultadoAtivo.size());
        assertEquals("Restaurante Italiano 1", resultadoAtivo.get(0).getNome());
        assertEquals("Restaurante Italiano 2", resultadoAtivo.get(1).getNome());
        assertTrue(resultadoAtivo.get(0).isAtivo());
        assertTrue(resultadoAtivo.get(1).isAtivo());
        assertEquals("Italiana", resultadoAtivo.get(0).getTipoCozinha());
        assertEquals("Italiana", resultadoAtivo.get(1).getTipoCozinha());
        assertEquals("08:00", resultadoAtivo.get(0).getHorarioAbertura());
        assertEquals("22:00", resultadoAtivo.get(0).getHorarioFechamento());
        assertEquals("09:00", resultadoAtivo.get(1).getHorarioAbertura());
        assertEquals("23:00", resultadoAtivo.get(1).getHorarioFechamento());
        assertEquals(10L, resultadoAtivo.get(0).getUsuarioDonoId());
        assertEquals(20L, resultadoAtivo.get(1).getUsuarioDonoId());
        verify(restauranteRepository, times(1)).findByAtivo(true, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void deveRetornarListaDeRestauranteQuandoForAtivoETipoCozinha() {
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);
        params.setTipoCozinha("Italiana");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano 1");
        restaurante1.setAtivo(true);
        restaurante1.setTipoCozinha("Italiana");

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Italiano 2");
        restaurante2.setAtivo(true);
        restaurante2.setTipoCozinha("Italiana");

        List<Restaurante> listaEsperada = List.of(restaurante1, restaurante2);

        when(restauranteRepository.findByAtivoAndTipoCozinha(true, "Italiana")).thenReturn(listaEsperada);

        ValidarParametroConsultaRestaurante validar = new ValidarParametroConsultaRestaurante(restauranteRepository);

        List<Restaurante> resultado = validar.validarParametrosConsultaRestaurante(params);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Restaurante Italiano 1", resultado.get(0).getNome());
        assertEquals("Restaurante Italiano 2", resultado.get(1).getNome());
        verify(restauranteRepository, times(1)).findByAtivoAndTipoCozinha(true, "Italiana");
    }

    @Test
    void deveRetornarTodosRestaurantesQuandoParametroNulo() {
        ListarRestaurantesParams params = new ListarRestaurantesParams();

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante 1");

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");

        List<Restaurante> listaEsperada = List.of(restaurante1, restaurante2);

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(restauranteRepository.findAll(sort)).thenReturn(listaEsperada);

        ValidarParametroConsultaRestaurante validar = new ValidarParametroConsultaRestaurante(restauranteRepository);

        List<Restaurante> resultado = validar.validarParametrosConsultaRestaurante(params);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Restaurante 1", resultado.get(0).getNome());
        assertEquals("Restaurante 2", resultado.get(1).getNome());
        verify(restauranteRepository, times(1)).findAll(sort);
    }

    @Test
    void deveRetornarListaDeRestauranteQuandoForInativo() {
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(false);

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano 1");
        restaurante1.setAtivo(false);
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setHorarioAbertura("08:00");
        restaurante1.setHorarioFechamento("22:00");
        restaurante1.setUsuarioDonoId(10L);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Italiano 2");
        restaurante2.setAtivo(false);
        restaurante2.setTipoCozinha("Italiana");
        restaurante2.setHorarioAbertura("09:00");
        restaurante2.setHorarioFechamento("23:00");
        restaurante2.setUsuarioDonoId(20L);

        List<Restaurante> listaInativa = List.of(restaurante1, restaurante2);

        when(restauranteRepository.findByAtivo(false, Sort.by(Sort.Direction.ASC, "id"))).thenReturn(listaInativa);

        ValidarParametroConsultaRestaurante validarInativo = new ValidarParametroConsultaRestaurante(
                restauranteRepository);

        List<Restaurante> resultadoInativo = validarInativo.validarParametrosConsultaRestaurante(params);
        assertNotNull(resultadoInativo);
        assertEquals(2, resultadoInativo.size());
        assertEquals("Restaurante Italiano 1", resultadoInativo.get(0).getNome());
        assertEquals("Restaurante Italiano 2", resultadoInativo.get(1).getNome());
        assertFalse(resultadoInativo.get(0).isAtivo());
        assertFalse(resultadoInativo.get(1).isAtivo());
        assertEquals("Italiana", resultadoInativo.get(0).getTipoCozinha());
        assertEquals("Italiana", resultadoInativo.get(1).getTipoCozinha());
        assertEquals("08:00", resultadoInativo.get(0).getHorarioAbertura());
        assertEquals("22:00", resultadoInativo.get(0).getHorarioFechamento());
        assertEquals("09:00", resultadoInativo.get(1).getHorarioAbertura());
        assertEquals("23:00", resultadoInativo.get(1).getHorarioFechamento());
        assertEquals(10L, resultadoInativo.get(0).getUsuarioDonoId());
        assertEquals(20L, resultadoInativo.get(1).getUsuarioDonoId());
        verify(restauranteRepository, times(1)).findByAtivo(false, Sort.by(Sort.Direction.ASC, "id"));
    }

}
