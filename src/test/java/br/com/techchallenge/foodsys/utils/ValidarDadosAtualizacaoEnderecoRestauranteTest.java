package br.com.techchallenge.foodsys.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

class ValidarDadosAtualizacaoEnderecoRestauranteTest {
    @Mock
    private ValidarCepRestauranteDuplicado validarCepRestauranteDuplicado;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;
    @Mock
    private ValidarCamposEndereco validarCamposEndereco;
    @Mock
    private ValidarDadosAtualizacaoEnderecoRestaurante validarDadosAtualizacaoEnderecoRestaurante;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validarDadosAtualizacaoEnderecoRestaurante = new ValidarDadosAtualizacaoEnderecoRestaurante(
                validarCepRestauranteDuplicado,
                validarEnderecoExistente,
                validarProprietarioEndereco,
                validarCamposEndereco);
    }

    @Test
    void deveValidarAtualizacaoComSucesso() {
        Long enderecoId = 1L;
        Long restauranteId = 2L;

        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Usuario usuario = new Usuario();
        usuario.setId(3L);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        doNothing().when(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuario.getId(),
                restauranteId);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(restauranteId, dto.getCep());

        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(enderecoId, dto, usuario);

        assertNotNull(resultado);
        assertEquals(enderecoId, resultado.getId());
        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuario.getId(), restauranteId);
        verify(validarCepRestauranteDuplicado).validarCep(restauranteId, dto.getCep());
    }

    @Test
    void deveLancarExcecaoQuandoCamposInvalidos() {
        Long enderecoId = 1L;
        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua(null);
        dto.setCep(null);
        dto.setNumero(null);
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(3L);

        doThrow(new BadRequestException("atualizar.endereco.nenhum.campo"))
                .when(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(enderecoId, dto, usuario));
        assertEquals("atualizar.endereco.nenhum.campo", exception.getMessage());
        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        verifyNoInteractions(validarEnderecoExistente, validarProprietarioEndereco, validarCepRestauranteDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoExiste() {
        Long enderecoId = 1L;
        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(3L);

        doNothing().when(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        doThrow(new BadRequestException("endereco.nao.encontrado"))
                .when(validarEnderecoExistente).execute(enderecoId);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(enderecoId, dto, usuario));
        assertEquals("endereco.nao.encontrado", exception.getMessage());
        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        verify(validarEnderecoExistente).execute(enderecoId);
        verifyNoInteractions(validarProprietarioEndereco, validarCepRestauranteDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEProprietario() {
        Long enderecoId = 1L;
        Long restauranteId = 2L;

        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Usuario usuario = new Usuario();
        usuario.setId(3L);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        doNothing().when(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doThrow(new BadRequestException("usuario.nao.proprietario"))
                .when(validarProprietarioEndereco)
                .validarProprietarioEndereco(endereco, usuario.getId(), restauranteId);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(enderecoId, dto, usuario));
        assertEquals("usuario.nao.proprietario", exception.getMessage());
        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuario.getId(), restauranteId);
        verifyNoInteractions(validarCepRestauranteDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        Long enderecoId = 1L;
        Long restauranteId = 2L;

        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Usuario usuario = new Usuario();
        usuario.setId(3L);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        doNothing().when(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuario.getId(),
                restauranteId);
        doThrow(new BadRequestException("cep.ja.cadastrado.para.restaurante"))
                .when(validarCepRestauranteDuplicado).validarCep(restauranteId, dto.getCep());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(enderecoId, dto, usuario));
        assertEquals("cep.ja.cadastrado.para.restaurante", exception.getMessage());
        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero());
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuario.getId(), restauranteId);
        verify(validarCepRestauranteDuplicado).validarCep(restauranteId, dto.getCep());
    }
}