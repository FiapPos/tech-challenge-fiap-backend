package br.com.techchallenge.foodsys.query.endereco;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarListaDeEndereco;

public class ListarEnderecosQueryTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarListaDeEndereco validarListaDeEndereco;
    @InjectMocks
    private ListarEnderecosQuery listarEnderecosQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarEnderecosPorUsuario() {

        Long usuarioId = 1L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setUsuarioId(usuarioId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(10L);
        endereco1.setRua("Rua A");
        endereco1.setCep("12345-000");
        endereco1.setNumero("100");
        endereco1.setDataCriacao(null);
        endereco1.setDataAtualizacao(null);

        Endereco endereco2 = new Endereco();
        endereco2.setId(20L);
        endereco2.setRua("Rua B");
        endereco2.setCep("54321-000");
        endereco2.setNumero("200");
        endereco2.setDataCriacao(null);
        endereco2.setDataAtualizacao(null);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        when(validarListaDeEndereco.listarEnderecos(usuarioId, null)).thenReturn(enderecos);

        List<ListarEnderecoPorResultadoItem> resultado = listarEnderecosQuery.execute(params);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(endereco1.getId(), resultado.get(0).getId());
        assertEquals(endereco2.getId(), resultado.get(1).getId());
        assertEquals(endereco1.getRua(), resultado.get(0).getRua());
        assertEquals(endereco2.getRua(), resultado.get(1).getRua());
        assertEquals(endereco1.getCep(), resultado.get(0).getCep());
        assertEquals(endereco2.getCep(), resultado.get(1).getCep());
        assertEquals(endereco1.getNumero(), resultado.get(0).getNumero());
        assertEquals(endereco2.getNumero(), resultado.get(1).getNumero());
        assertNull(resultado.get(0).getDataCriacao());
        assertNull(resultado.get(1).getDataCriacao());
        assertNull(resultado.get(0).getDataAtualizacao());
        assertNull(resultado.get(1).getDataAtualizacao());

        verify(validarListaDeEndereco).listarEnderecos(usuarioId, null);
    }

    @Test
    void deveListarEnderecosPorRestaurante() {

        Long restauranteId = 5L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setRestauranteId(restauranteId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(30L);
        endereco1.setRua("Rua C");
        endereco1.setCep("11111-000");
        endereco1.setNumero("300");
        endereco1.setDataCriacao(null);
        endereco1.setDataAtualizacao(null);

        Endereco endereco2 = new Endereco();
        endereco2.setId(40L);
        endereco2.setRua("Rua D");
        endereco2.setCep("22222-000");
        endereco2.setNumero("400");
        endereco2.setDataCriacao(null);
        endereco2.setDataAtualizacao(null);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        when(validarListaDeEndereco.listarEnderecos(null, restauranteId)).thenReturn(enderecos);

        List<ListarEnderecoPorResultadoItem> resultado = listarEnderecosQuery.execute(params);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(endereco1.getId(), resultado.get(0).getId());
        assertEquals(endereco2.getId(), resultado.get(1).getId());
        assertEquals(endereco1.getRua(), resultado.get(0).getRua());
        assertEquals(endereco2.getRua(), resultado.get(1).getRua());
        assertEquals(endereco1.getCep(), resultado.get(0).getCep());
        assertEquals(endereco2.getCep(), resultado.get(1).getCep());
        assertEquals(endereco1.getNumero(), resultado.get(0).getNumero());
        assertEquals(endereco2.getNumero(), resultado.get(1).getNumero());
        assertNull(resultado.get(0).getDataCriacao());
        assertNull(resultado.get(1).getDataCriacao());
        assertNull(resultado.get(0).getDataAtualizacao());
        assertNull(resultado.get(1).getDataAtualizacao());

        verify(validarListaDeEndereco).listarEnderecos(null, restauranteId);
    }

    @Test
    void deveMapearCorretamenteOsDtos() {
        Long usuarioId = 1L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setUsuarioId(usuarioId);

        Endereco endereco = new Endereco();
        endereco.setId(123L);
        endereco.setRua("Rua Teste");
        endereco.setCep("99999-999");
        endereco.setNumero("321");
        endereco.setDataCriacao(java.time.LocalDateTime.of(2023, 5, 10, 12, 0));
        endereco.setDataAtualizacao(java.time.LocalDateTime.of(2023, 6, 15, 15, 30));

        List<Endereco> enderecos = List.of(endereco);

        when(validarListaDeEndereco.listarEnderecos(usuarioId, null)).thenReturn(enderecos);

        List<ListarEnderecoPorResultadoItem> resultado = listarEnderecosQuery.execute(params);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        ListarEnderecoPorResultadoItem dto = resultado.get(0);
        assertEquals(endereco.getId(), dto.getId());
        assertEquals(endereco.getRua(), dto.getRua());
        assertEquals(endereco.getCep(), dto.getCep());
        assertEquals(endereco.getNumero(), dto.getNumero());
        assertEquals(endereco.getDataCriacao(), dto.getDataCriacao());
        assertEquals(endereco.getDataAtualizacao(), dto.getDataAtualizacao());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaEnderecos() {
        Long usuarioId = 1L;
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setUsuarioId(usuarioId);

        when(validarListaDeEndereco.listarEnderecos(usuarioId, null)).thenReturn(List.of());

        List<ListarEnderecoPorResultadoItem> resultado = listarEnderecosQuery.execute(params);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioeRestauranteNulos() {
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setUsuarioId(null);
        params.setRestauranteId(null);

        // Configura o mock para lançar a exceção esperada
        when(validarListaDeEndereco.listarEnderecos(null, null))
                .thenThrow(new BadRequestException("usuario.id.ou.restaurante.id.obrigatorio"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> listarEnderecosQuery.execute(params));
        assertEquals("usuario.id.ou.restaurante.id.obrigatorio", exception.getMessage());
    }
}
