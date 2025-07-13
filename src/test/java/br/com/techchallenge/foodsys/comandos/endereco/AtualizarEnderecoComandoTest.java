package br.com.techchallenge.foodsys.comandos.endereco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarCepDuplicado;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

public class AtualizarEnderecoComandoTest {

    @InjectMocks
    private AtualizarEnderecoComando atualizarEnderecoComando;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private CompartilhadoService sharedService;
    @Mock
    private ValidarCepDuplicado validarCepDuplicado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAtualizarEnderecoComCamposCorretos() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), dto.getCep());
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        AtualizarEnderecoComando comando = new AtualizarEnderecoComando(
                enderecoRepository,
                validarEnderecoExistente,
                sharedService,
                validarCepDuplicado);

        Endereco resultado = comando.execute(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals("Nova Rua", resultado.getRua());
        assertEquals("12345-678", resultado.getCep());
        assertEquals("100", resultado.getNumero());
        assertEquals(enderecoId, resultado.getId());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(dto.getRestauranteId(), resultado.getRestaurante().getId());
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), dto.getCep());
        verify(enderecoRepository).save(endereco);
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        assertThrows(BadRequestException.class, () -> atualizarEnderecoComando.execute(1L, dto, usuario));
    }

    @Test
    void deveValidarCepDuplicado() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setCep("12345-678");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        atualizarEnderecoComando.execute(enderecoId, dto, usuario);
        verify(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), dto.getCep());
    }

    @Test
    void deveAtualizarApenasRuaQuandoApenasRuaPreenchida() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Rua Atualizada");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), null);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        AtualizarEnderecoComando comando = new AtualizarEnderecoComando(
                enderecoRepository,
                validarEnderecoExistente,
                sharedService,
                validarCepDuplicado);

        Endereco resultado = comando.execute(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals("Rua Atualizada", resultado.getRua());
        assertEquals("00000-000", resultado.getCep());
        assertEquals("50", resultado.getNumero());
        assertEquals(enderecoId, resultado.getId());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(dto.getRestauranteId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveAtualizarApenasQuandoCepPreenchido() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setCep("54321-987");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), null);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        AtualizarEnderecoComando comando = new AtualizarEnderecoComando(
                enderecoRepository,
                validarEnderecoExistente,
                sharedService,
                validarCepDuplicado);

        Endereco resultado = comando.execute(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals("Rua Antiga", resultado.getRua());
        assertEquals("54321-987", resultado.getCep());
        assertEquals("50", resultado.getNumero());
        assertEquals(enderecoId, resultado.getId());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(dto.getRestauranteId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveAtualizarApenasQuandoNumeroPreenchido() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setNumero("200");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), null);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        AtualizarEnderecoComando comando = new AtualizarEnderecoComando(
                enderecoRepository,
                validarEnderecoExistente,
                sharedService,
                validarCepDuplicado);

        Endereco resultado = comando.execute(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals("Rua Antiga", resultado.getRua());
        assertEquals("00000-000", resultado.getCep());
        assertEquals("200", resultado.getNumero());
        assertEquals(enderecoId, resultado.getId());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(dto.getRestauranteId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveAtualizarRuaECepQuandoApenasEssesCamposPreenchidos() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setCep("54321-987");
        dto.setRua("Rua Atualizada");
        dto.setRestauranteId(3L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getRestauranteId());

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, dto.getRestauranteId(), null);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        AtualizarEnderecoComando comando = new AtualizarEnderecoComando(
                enderecoRepository,
                validarEnderecoExistente,
                sharedService,
                validarCepDuplicado);

        Endereco resultado = comando.execute(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals("Rua Atualizada", resultado.getRua());
        assertEquals("54321-987", resultado.getCep());
        assertEquals("50", resultado.getNumero());
        assertEquals(enderecoId, resultado.getId());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(dto.getRestauranteId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveLancarExcecaoQuandoTodosCamposNulos() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        assertThrows(BadRequestException.class, () -> atualizarEnderecoComando.execute(1L, dto, usuario));
    }
}
