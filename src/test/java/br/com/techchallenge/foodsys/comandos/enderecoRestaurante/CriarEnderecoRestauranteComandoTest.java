package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosCriacaoEnderecoRestaurante;

public class CriarEnderecoRestauranteComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private CompartilhadoService sharedService;
    @Mock
    private ValidarDadosCriacaoEnderecoRestaurante validarDadosDeEndereco;

    @InjectMocks
    private CriarEnderecoRestauranteComando criarEnderecoComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoParaRestaurante() {
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRua("Rua Restaurante");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Endereco enderecoEsperado = new Endereco();
        enderecoEsperado.setRua(dto.getRua());
        enderecoEsperado.setCep(dto.getCep());
        enderecoEsperado.setNumero(dto.getNumero());
        enderecoEsperado.setUsuario(usuario);
        enderecoEsperado.setRestaurante(restaurante);

        when(validarDadosDeEndereco.validarCriacao(dto, usuario)).thenReturn(restaurante);
        when(sharedService.getCurrentDateTime()).thenReturn(java.time.LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoEsperado);

        Endereco resultado = criarEnderecoComando.execute(dto, usuario);

        assertNotNull(resultado);
        assertEquals(dto.getRua(), resultado.getRua());
        assertEquals(dto.getCep(), resultado.getCep());
        assertEquals(dto.getNumero(), resultado.getNumero());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(restauranteId, resultado.getRestaurante().getId());

        verify(validarDadosDeEndereco).validarCriacao(dto, usuario);
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveLancarExcecaoQuandoDadosInvalidos() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRua(null);
        dto.setCep(null);
        dto.setNumero(null);
        dto.setRestauranteId(null);

        doThrow(new IllegalArgumentException("Dados inválidos"))
                .when(validarDadosDeEndereco).validarCriacao(dto, usuario);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> criarEnderecoComando.execute(dto, usuario));
        assertEquals("Dados inválidos", exception.getMessage());

        verify(validarDadosDeEndereco).validarCriacao(dto, usuario);
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }
}