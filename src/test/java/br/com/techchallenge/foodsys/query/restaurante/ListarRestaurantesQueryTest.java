package br.com.techchallenge.foodsys.query.restaurante;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import br.com.techchallenge.foodsys.infrastructure.api.controllers.RestauranteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.techchallenge.foodsys.query.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.query.resultadoItem.restaurante.ListarRestaurantesResultadoItem;

public class ListarRestaurantesQueryTest {

    @Mock
    private ListarRestaurantesQuery listarRestaurantesQuery;

    @InjectMocks
    private RestauranteController restauranteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarRestaurantePorTipoCozinha() {

        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setTipoCozinha("Italiana");

        ListarRestaurantesResultadoItem item = new ListarRestaurantesResultadoItem();
        item.setId(1L);
        item.setNome("Restaurante Italiano");
        item.setTipoCozinha("Italiana");
        item.setAtivo(true);
        item.setHorarioAbertura("08:00");
        item.setHorarioFechamento("22:00");
        item.setUsuarioDonoId(10L);
        item.setDataCriacao(null);
        item.setDataAtualizacao(null);
        item.setDataDesativacao(null); // Incluindo a data de desativação como null

        List<ListarRestaurantesResultadoItem> resultado = List.of(item);

        when(listarRestaurantesQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        ListarRestaurantesResultadoItem resultItem = response.getBody().get(0);
        assertEquals(1L, resultItem.getId());
        assertEquals("Restaurante Italiano", resultItem.getNome());
        assertEquals("Italiana", resultItem.getTipoCozinha());
        assertTrue(resultItem.getAtivo());
        assertEquals("08:00", resultItem.getHorarioAbertura());
        assertEquals("22:00", resultItem.getHorarioFechamento());
        assertEquals(10L, resultItem.getUsuarioDonoId());
        assertNull(resultItem.getDataCriacao());
        assertNull(resultItem.getDataAtualizacao());
        assertNull(resultItem.getDataDesativacao());
        verify(listarRestaurantesQuery, times(1)).execute(params);
    }

    @Test
    void deveListarRestauranteQuandoForAtivo() {

        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(true);

        ListarRestaurantesResultadoItem item = new ListarRestaurantesResultadoItem();
        item.setId(2L);
        item.setNome("Restaurante Ativo");
        item.setTipoCozinha("Brasileira");
        item.setAtivo(true);
        item.setHorarioAbertura("09:00");
        item.setHorarioFechamento("21:00");
        item.setUsuarioDonoId(20L);
        item.setDataCriacao(null);
        item.setDataAtualizacao(null);
        item.setDataDesativacao(null);

        List<ListarRestaurantesResultadoItem> resultado = List.of(item);

        when(listarRestaurantesQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        ListarRestaurantesResultadoItem resultItem = response.getBody().get(0);
        assertEquals(2L, resultItem.getId());
        assertEquals("Restaurante Ativo", resultItem.getNome());
        assertEquals("Brasileira", resultItem.getTipoCozinha());
        assertTrue(resultItem.getAtivo());
        assertEquals("09:00", resultItem.getHorarioAbertura());
        assertEquals("21:00", resultItem.getHorarioFechamento());
        assertEquals(20L, resultItem.getUsuarioDonoId());
        assertNull(resultItem.getDataCriacao());
        assertNull(resultItem.getDataAtualizacao());
        assertNull(resultItem.getDataDesativacao());
        verify(listarRestaurantesQuery, times(1)).execute(params);
    }

    @Test
    void deveListarRestauranteQuandoForInativo() {

        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setAtivo(false);

        ListarRestaurantesResultadoItem item = new ListarRestaurantesResultadoItem();
        item.setId(3L);
        item.setNome("Restaurante Inativo");
        item.setTipoCozinha("Japonesa");
        item.setAtivo(false);
        item.setHorarioAbertura("10:00");
        item.setHorarioFechamento("20:00");
        item.setUsuarioDonoId(30L);
        item.setDataCriacao(null);
        item.setDataAtualizacao(null);
        // Incluindo a data de desativação
        LocalDateTime dataDesativacao = LocalDateTime.of(2024, 7, 10, 12, 0);
        item.setDataDesativacao(dataDesativacao);

        List<ListarRestaurantesResultadoItem> resultado = List.of(item);

        when(listarRestaurantesQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        ListarRestaurantesResultadoItem resultItem = response.getBody().get(0);
        assertEquals(3L, resultItem.getId());
        assertEquals("Restaurante Inativo", resultItem.getNome());
        assertEquals("Japonesa", resultItem.getTipoCozinha());
        assertFalse(resultItem.getAtivo());
        assertEquals("10:00", resultItem.getHorarioAbertura());
        assertEquals("20:00", resultItem.getHorarioFechamento());
        assertEquals(30L, resultItem.getUsuarioDonoId());
        assertNull(resultItem.getDataCriacao());
        assertNull(resultItem.getDataAtualizacao());
        assertEquals(dataDesativacao, resultItem.getDataDesativacao());
        verify(listarRestaurantesQuery, times(1)).execute(params);
    }

    @Test
    void deveListarTodosRestaurantesQuandoParametroForNull() {

        ListarRestaurantesParams params = new ListarRestaurantesParams(); // todos os filtros null

        ListarRestaurantesResultadoItem item1 = new ListarRestaurantesResultadoItem();
        item1.setId(1L);
        item1.setNome("Restaurante 1");
        item1.setTipoCozinha("Italiana");
        item1.setAtivo(true);
        item1.setHorarioAbertura("08:00");
        item1.setHorarioFechamento("22:00");
        item1.setUsuarioDonoId(10L);

        ListarRestaurantesResultadoItem item2 = new ListarRestaurantesResultadoItem();
        item2.setId(2L);
        item2.setNome("Restaurante 2");
        item2.setTipoCozinha("Japonesa");
        item2.setAtivo(false);
        item2.setHorarioAbertura("10:00");
        item2.setHorarioFechamento("20:00");
        item2.setUsuarioDonoId(20L);

        List<ListarRestaurantesResultadoItem> resultado = List.of(item1, item2);

        when(listarRestaurantesQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        ListarRestaurantesResultadoItem resultItem1 = response.getBody().get(0);
        assertEquals(1L, resultItem1.getId());
        assertEquals("Restaurante 1", resultItem1.getNome());
        assertEquals("Italiana", resultItem1.getTipoCozinha());
        assertTrue(resultItem1.getAtivo());
        assertEquals("08:00", resultItem1.getHorarioAbertura());
        assertEquals("22:00", resultItem1.getHorarioFechamento());
        assertEquals(10L, resultItem1.getUsuarioDonoId());

        ListarRestaurantesResultadoItem resultItem2 = response.getBody().get(1);
        assertEquals(2L, resultItem2.getId());
        assertEquals("Restaurante 2", resultItem2.getNome());
        assertEquals("Japonesa", resultItem2.getTipoCozinha());
        assertFalse(resultItem2.getAtivo());
        assertEquals("10:00", resultItem2.getHorarioAbertura());
        assertEquals("20:00", resultItem2.getHorarioFechamento());
        assertEquals(20L, resultItem2.getUsuarioDonoId());

        verify(listarRestaurantesQuery, times(1)).execute(params);
    }
}
