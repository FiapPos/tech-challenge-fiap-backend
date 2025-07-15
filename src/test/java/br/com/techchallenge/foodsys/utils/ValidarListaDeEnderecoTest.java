package br.com.techchallenge.foodsys.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

public class ValidarListaDeEnderecoTest {
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @InjectMocks
    private ValidarListaDeEndereco validarListaDeEndereco;

    @BeforeEach
    void setUp() {
        enderecoRepository = mock(EnderecoRepository.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        validarRestauranteExistente = mock(ValidarRestauranteExistente.class);
        validarListaDeEndereco = new ValidarListaDeEndereco(
                enderecoRepository,
                validarUsuarioExistente,
                validarRestauranteExistente);
    }

    @Test
    void deveRetornarListaEnderecosPorUsuario() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(10L);
        endereco1.setRua("Rua A");

        Endereco endereco2 = new Endereco();
        endereco2.setId(20L);
        endereco2.setRua("Rua B");

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(enderecoRepository.findByUsuarioId(usuarioId, sort)).thenReturn(enderecos);

        List<Endereco> resultado = validarListaDeEndereco.listarEnderecos(usuarioId, null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Rua A", resultado.get(0).getRua());
        assertEquals("Rua B", resultado.get(1).getRua());

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(enderecoRepository).findByUsuarioId(usuarioId, sort);
        verifyNoInteractions(validarRestauranteExistente);
    }

    @Test
    void deveRetornarEnderecosPorRestaurante() {
        Long restauranteId = 2L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(100L);
        endereco1.setRua("Rua Restaurante 1");

        Endereco endereco2 = new Endereco();
        endereco2.setId(200L);
        endereco2.setRua("Rua Restaurante 2");

        List<Endereco> enderecos = List.of(endereco1, endereco2);

        // Mocka a validação do restaurante existente
        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);

        // Mocka a chamada do repository
        when(enderecoRepository.findByRestauranteId(restauranteId)).thenReturn(enderecos);

        List<Endereco> resultado = validarListaDeEndereco.listarEnderecos(null, restauranteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Rua Restaurante 1", resultado.get(0).getRua());
        assertEquals("Rua Restaurante 2", resultado.get(1).getRua());

        verify(validarRestauranteExistente).execute(restauranteId);
        verify(enderecoRepository).findByRestauranteId(restauranteId);
        verifyNoInteractions(validarUsuarioExistente);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioERestauranteNulos() {
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validarListaDeEndereco.listarEnderecos(null, null));
        assertEquals("usuario.id.ou.restaurante.id.obrigatorio", exception.getMessage());
        verifyNoInteractions(validarUsuarioExistente, validarRestauranteExistente, enderecoRepository);
    }

}
