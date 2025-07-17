package br.com.techchallenge.foodsys.query.enderecoRestaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarListaDeEnderecoRestaurante;

public class ListarEnderecoRestauranteQueryTest {

    @Mock
    private ValidarListaDeEnderecoRestaurante validarListaDeEnderecoRestaurante;

    private ListarEnderecoRestauranteQuery listarEnderecoRestauranteQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listarEnderecoRestauranteQuery = new ListarEnderecoRestauranteQuery(validarListaDeEnderecoRestaurante);
    }

    @Test
    void deveRetornarListaDeEnderecosPorRestaurante() {
        Long restauranteId = 10L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setRestauranteId(restauranteId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(1L);
        endereco1.setRua("Rua 1");
        endereco1.setCep("12345-000");
        endereco1.setNumero("10");
        endereco1.setDataCriacao(LocalDateTime.now());
        endereco1.setDataAtualizacao(LocalDateTime.now());

        Endereco endereco2 = new Endereco();
        endereco2.setId(2L);
        endereco2.setRua("Rua 2");
        endereco2.setCep("54321-000");
        endereco2.setNumero("20");
        endereco2.setDataCriacao(LocalDateTime.now());
        endereco2.setDataAtualizacao(LocalDateTime.now());

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        when(validarListaDeEnderecoRestaurante.listarPorRestauranteId(restauranteId)).thenReturn(enderecos);

        List<ListarEnderecoPorRestauranteResultadoItem> resultado = listarEnderecoRestauranteQuery.execute(params);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Rua 1", resultado.get(0).getRua());
        assertEquals("12345-000", resultado.get(0).getCep());
        assertEquals("10", resultado.get(0).getNumero());

        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Rua 2", resultado.get(1).getRua());
        assertEquals("54321-000", resultado.get(1).getCep());
        assertEquals("20", resultado.get(1).getNumero());

        verify(validarListaDeEnderecoRestaurante).listarPorRestauranteId(restauranteId);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemEnderecos() {
        Long restauranteId = 99L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setRestauranteId(restauranteId);

        when(validarListaDeEnderecoRestaurante.listarPorRestauranteId(restauranteId)).thenReturn(List.of());

        List<ListarEnderecoPorRestauranteResultadoItem> resultado = listarEnderecoRestauranteQuery.execute(params);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(validarListaDeEnderecoRestaurante).listarPorRestauranteId(restauranteId);
    }
}
