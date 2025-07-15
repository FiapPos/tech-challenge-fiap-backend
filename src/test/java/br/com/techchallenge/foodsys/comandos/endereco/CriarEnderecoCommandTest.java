package br.com.techchallenge.foodsys.comandos.endereco;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosCriacaoEndereco;

public class CriarEnderecoCommandTest {

    @InjectMocks
    private CriarEnderecoCommand criarEnderecoComand;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private CompartilhadoService sharedService;
    @Mock
    private ValidarDadosCriacaoEndereco validarDadosDeEndereco;

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

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
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

        doNothing().when(validarDadosDeEndereco).validarCriacao(dto, usuario);
        when(validarDadosDeEndereco.obterRestauranteSeNecessario(dto, usuario)).thenReturn(restaurante);
        when(sharedService.getCurrentDateTime()).thenReturn(java.time.LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoEsperado);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarDadosDeEndereco);

        Endereco resultado = comando.execute(dto, usuario);

        assertNotNull(resultado);
        assertEquals(dto.getRua(), resultado.getRua());
        assertEquals(dto.getCep(), resultado.getCep());
        assertEquals(dto.getNumero(), resultado.getNumero());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(restauranteId, resultado.getRestaurante().getId());

        verify(validarDadosDeEndereco).validarCriacao(dto, usuario);
        verify(validarDadosDeEndereco).obterRestauranteSeNecessario(dto, usuario);
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveCriarEnderecoParaUsuario() {
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Usuário");
        dto.setCep("11111-111");
        dto.setNumero("99");
        dto.setRestauranteId(null);

        Endereco enderecoEsperado = new Endereco();
        enderecoEsperado.setRua(dto.getRua());
        enderecoEsperado.setCep(dto.getCep());
        enderecoEsperado.setNumero(dto.getNumero());
        enderecoEsperado.setUsuario(usuario);
        enderecoEsperado.setRestaurante(null);

        doNothing().when(validarDadosDeEndereco).validarCriacao(dto, usuario);
        when(validarDadosDeEndereco.obterRestauranteSeNecessario(dto, usuario)).thenReturn(null);
        when(sharedService.getCurrentDateTime()).thenReturn(java.time.LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoEsperado);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarDadosDeEndereco);

        Endereco resultado = comando.execute(dto, usuario);

        assertNotNull(resultado);
        assertEquals(dto.getRua(), resultado.getRua());
        assertEquals(dto.getCep(), resultado.getCep());
        assertEquals(dto.getNumero(), resultado.getNumero());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertNull(resultado.getRestaurante());

        verify(validarDadosDeEndereco).validarCriacao(dto, usuario);
        verify(validarDadosDeEndereco).obterRestauranteSeNecessario(dto, usuario);
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveLancarExcecaoQuandoDadosInvalidos() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua(null);
        dto.setCep(null);
        dto.setNumero(null);
        dto.setRestauranteId(null);

        doThrow(new IllegalArgumentException("Dados inválidos"))
                .when(validarDadosDeEndereco).validarCriacao(dto, usuario);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarDadosDeEndereco);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> comando.execute(dto, usuario));
        assertEquals("Dados inválidos", exception.getMessage());

        verify(validarDadosDeEndereco).validarCriacao(dto, usuario);
        verify(validarDadosDeEndereco, never()).obterRestauranteSeNecessario(dto, usuario);
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }
}