package br.com.techchallenge.foodsys.query.endereco;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.springframework.data.domain.Sort;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;

public class ListarEnderecosQueryTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarEnderecosPorUsuario() {
        // Arrange
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

        when(enderecoRepository.findByUsuarioId(eq(usuarioId), any(Sort.class))).thenReturn(enderecos);
        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);

        ListarEnderecosQuery query = new ListarEnderecosQuery(enderecoRepository, validarUsuarioExistente,
                validarRestauranteExistente);

        // Act
        List<ListarEnderecoPorResultadoItem> resultado = query.execute(params);

        // Assert
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

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(enderecoRepository).findByUsuarioId(eq(usuarioId), any(Sort.class));
    }

    @Test
    void deveListarEnderecosPorRestaurante() {
        // Arrange
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

        when(enderecoRepository.findByRestauranteId(restauranteId)).thenReturn(enderecos);
        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);

        ListarEnderecosQuery query = new ListarEnderecosQuery(enderecoRepository, validarUsuarioExistente,
                validarRestauranteExistente);

        // Act
        List<ListarEnderecoPorResultadoItem> resultado = query.execute(params);

        // Assert
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

        verify(validarRestauranteExistente).execute(restauranteId);
        verify(enderecoRepository).findByRestauranteId(restauranteId);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioERestauranteNulo() {
        // Arrange
        ListarEnderecosParams params = new ListarEnderecosParams();
        params.setUsuarioId(null);
        params.setRestauranteId(null);

        ListarEnderecosQuery query = new ListarEnderecosQuery(enderecoRepository, validarUsuarioExistente,
                validarRestauranteExistente);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> query.execute(params));
        assertEquals("usuario.id.ou.restaurante.id.obrigatorio", exception.getMessage());
    }
}
