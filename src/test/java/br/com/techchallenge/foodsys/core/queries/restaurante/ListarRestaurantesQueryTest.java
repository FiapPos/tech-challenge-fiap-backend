package br.com.techchallenge.foodsys.core.queries.restaurante;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.queries.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.core.utils.ValidarParametroConsultaRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarRestaurantesQueryTest {

    @Mock
    private ValidarParametroConsultaRestaurante validarParametroConsultaRestaurante;

    @InjectMocks
    private ListarRestaurantesQuery listarRestaurantesQuery;

    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup usuários
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        
        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Maria Santos");
        
        // Setup restaurantes
        restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano");
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setHorarioAbertura("08:00");
        restaurante1.setHorarioFechamento("22:00");
        restaurante1.setAtivo(true);
        restaurante1.setUsuario(usuario1);
        restaurante1.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));
        restaurante1.setDataAtualizacao(LocalDateTime.of(2024, 1, 15, 14, 30));
        restaurante1.setDataDesativacao(null);
        
        restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Japonês");
        restaurante2.setTipoCozinha("Japonesa");
        restaurante2.setHorarioAbertura("10:00");
        restaurante2.setHorarioFechamento("20:00");
        restaurante2.setAtivo(false);
        restaurante2.setUsuario(usuario2);
        restaurante2.setDataCriacao(LocalDateTime.of(2024, 2, 1, 9, 0));
        restaurante2.setDataAtualizacao(LocalDateTime.of(2024, 2, 10, 16, 45));
        restaurante2.setDataDesativacao(LocalDateTime.of(2024, 3, 1, 12, 0));
    }

    @Test
    void deveListarRestaurantesComSucesso() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setTipoCozinha("Italiana");
        
        List<Restaurante> restaurantes = Arrays.asList(restaurante1);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act
        List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        ListarRestaurantesResultadoItem item = resultado.get(0);
        assertEquals(1L, item.getId());
        assertEquals("Restaurante Italiano", item.getNome());
        assertEquals(1L, item.getUsuarioDonoId());
        assertEquals("Italiana", item.getTipoCozinha());
        assertEquals("08:00", item.getHorarioAbertura());
        assertEquals("22:00", item.getHorarioFechamento());
        assertTrue(item.getAtivo());
        assertEquals(restaurante1.getDataCriacao(), item.getDataCriacao());
        assertEquals(restaurante1.getDataAtualizacao(), item.getDataAtualizacao());
        assertNull(item.getDataDesativacao());
        
        // Verify
        verify(validarParametroConsultaRestaurante).validarParametrosConsultaRestaurante(params);
    }

    @Test
    void deveListarMultiplosRestaurantesComSucesso() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        
        List<Restaurante> restaurantes = Arrays.asList(restaurante1, restaurante2);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act
        List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        ListarRestaurantesResultadoItem item1 = resultado.get(0);
        assertEquals(1L, item1.getId());
        assertEquals("Restaurante Italiano", item1.getNome());
        assertTrue(item1.getAtivo());
        
        ListarRestaurantesResultadoItem item2 = resultado.get(1);
        assertEquals(2L, item2.getId());
        assertEquals("Restaurante Japonês", item2.getNome());
        assertFalse(item2.getAtivo());
        assertEquals(restaurante2.getDataDesativacao(), item2.getDataDesativacao());
        
        // Verify
        verify(validarParametroConsultaRestaurante).validarParametrosConsultaRestaurante(params);
    }

    @Test
    void deveLancarExcecaoQuandoListaVazia() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        
        List<Restaurante> restaurantes = Arrays.asList();
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> listarRestaurantesQuery.execute(params));
        
        assertEquals("nenhum.restaurante.encontrado", exception.getMessage());
        
        // Verify
        verify(validarParametroConsultaRestaurante).validarParametrosConsultaRestaurante(params);
    }

    @Test
    void deveLancarExcecaoQuandoListaNula() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> listarRestaurantesQuery.execute(params));
        
        // Verify
        verify(validarParametroConsultaRestaurante).validarParametrosConsultaRestaurante(params);
    }

    @Test
    void deveMapearRestauranteComTodosOsCamposPreenchidos() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        
        List<Restaurante> restaurantes = Arrays.asList(restaurante2);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act
        List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        ListarRestaurantesResultadoItem item = resultado.get(0);
        assertEquals(2L, item.getId());
        assertEquals("Restaurante Japonês", item.getNome());
        assertEquals(2L, item.getUsuarioDonoId());
        assertEquals("Japonesa", item.getTipoCozinha());
        assertEquals("10:00", item.getHorarioAbertura());
        assertEquals("20:00", item.getHorarioFechamento());
        assertFalse(item.getAtivo());
        assertEquals(restaurante2.getDataCriacao(), item.getDataCriacao());
        assertEquals(restaurante2.getDataAtualizacao(), item.getDataAtualizacao());
        assertEquals(restaurante2.getDataDesativacao(), item.getDataDesativacao());
    }

    @Test
    void deveMapearRestauranteComCamposNulos() {
        // Arrange
        Restaurante restauranteComNulos = new Restaurante();
        restauranteComNulos.setId(3L);
        restauranteComNulos.setNome("Restaurante Teste");
        restauranteComNulos.setTipoCozinha(null);
        restauranteComNulos.setHorarioAbertura(null);
        restauranteComNulos.setHorarioFechamento(null);
        restauranteComNulos.setAtivo(true);
        restauranteComNulos.setUsuario(usuario1);
        restauranteComNulos.setDataCriacao(null);
        restauranteComNulos.setDataAtualizacao(null);
        restauranteComNulos.setDataDesativacao(null);
        
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        List<Restaurante> restaurantes = Arrays.asList(restauranteComNulos);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act
        List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        ListarRestaurantesResultadoItem item = resultado.get(0);
        assertEquals(3L, item.getId());
        assertEquals("Restaurante Teste", item.getNome());
        assertEquals(1L, item.getUsuarioDonoId());
        assertNull(item.getTipoCozinha());
        assertNull(item.getHorarioAbertura());
        assertNull(item.getHorarioFechamento());
        assertTrue(item.getAtivo());
        assertNull(item.getDataCriacao());
        assertNull(item.getDataAtualizacao());
        assertNull(item.getDataDesativacao());
    }

    @Test
    void deveMapearRestauranteComUsuarioNulo() {
        // Arrange
        Restaurante restauranteSemUsuario = new Restaurante();
        restauranteSemUsuario.setId(4L);
        restauranteSemUsuario.setNome("Restaurante Sem Usuário");
        restauranteSemUsuario.setUsuario(null);
        
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        List<Restaurante> restaurantes = Arrays.asList(restauranteSemUsuario);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> listarRestaurantesQuery.execute(params));
    }

    @Test
    void deveChamarValidarParametroConsultaRestauranteComParamsCorretos() {
        // Arrange
        ListarRestaurantesParams params = new ListarRestaurantesParams();
        params.setTipoCozinha("Italiana");
        params.setAtivo(true);
        
        List<Restaurante> restaurantes = Arrays.asList(restaurante1);
        when(validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params))
            .thenReturn(restaurantes);

        // Act
        listarRestaurantesQuery.execute(params);

        // Verify
        verify(validarParametroConsultaRestaurante).validarParametrosConsultaRestaurante(params);
    }
}
