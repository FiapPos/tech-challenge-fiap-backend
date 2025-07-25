package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.AtualizarRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.CriarRestauranteCommand;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.DesativarRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.dtos.restaurante.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.core.queries.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.core.queries.restaurante.ListarRestaurantesQuery;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.techchallenge.foodsys.core.dtos.restaurante.AtualizarRestauranteComandoDto;

public class RestauranteControllerTest {

    @Mock
    private CriarRestauranteCommand criarRestauranteCommand;
    @Mock
    private DesativarRestauranteComando desativarRestauranteComando;
    @Mock
    private AtualizarRestauranteComando atualizarRestauranteComando;
    @Mock
    private AutorizacaoService autorizacaoService;
    @Mock
    private ListarRestaurantesQuery listarRestaurantesQuery;

    @InjectMocks
    private RestauranteController restauranteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarRestauranteComSucesso() {
        CriarRestauranteCommandDto dto = new CriarRestauranteCommandDto();

        dto.setNome("Restaurante Teste");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        when(criarRestauranteCommand.execute(dto, usuario)).thenReturn(restaurante);

        ResponseEntity<Void> response = restauranteController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(usuario.getId());
        verify(criarRestauranteCommand, times(1)).execute(dto, usuario);
    }

    @Test
    void deveDesativarRestauranteComSucesso() {
        Long restauranteId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(desativarRestauranteComando.execute(restauranteId)).thenReturn(new Restaurante());

        ResponseEntity<Void> response = restauranteController.desativarRestaurante(restauranteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(usuario.getId());
        verify(desativarRestauranteComando, times(1)).execute(restauranteId);
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        Long restauranteId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setNome("Restaurante Atualizado");
        dto.setHorarioAbertura("09:00");
        dto.setHorarioFechamento("21:00");
        dto.setTipoCozinha("Mexicana");

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(atualizarRestauranteComando.execute(restauranteId, dto, usuario)).thenReturn(new Restaurante());

        ResponseEntity<Void> response = restauranteController.atualizar(restauranteId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(usuario.getId());
        verify(atualizarRestauranteComando, times(1)).execute(restauranteId, dto, usuario);
    }

    @Test
    void deveListarRestaurantesComSucesso() {

        ListarRestaurantesParams params = new ListarRestaurantesParams();
        List<ListarRestaurantesResultadoItem> resultado = List.of(new ListarRestaurantesResultadoItem());
        when(listarRestaurantesQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(listarRestaurantesQuery, times(1)).execute(params);

    }

    @Test
    void deveRetornarNoContentQuandoListaRestaurantesVazia() {

        ListarRestaurantesParams params = new ListarRestaurantesParams();
        when(listarRestaurantesQuery.execute(params)).thenReturn(List.of());

        ResponseEntity<List<ListarRestaurantesResultadoItem>> response = restauranteController
                .listarRestaurantes(params);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(listarRestaurantesQuery, times(1)).execute(params);
    }
}
