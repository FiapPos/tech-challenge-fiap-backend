package br.com.techchallenge.foodsys.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

class ValidarDadosCriacaoEnderecoRestauranteTest {

    @Mock
    private ValidarCepRestauranteDuplicado validarCepDuplicado;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private ValidarProprietarioRestaurante validarProprietarioRestaurante;

    private ValidarDadosCriacaoEnderecoRestaurante validarDadosCriacaoEnderecoRestaurante;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validarDadosCriacaoEnderecoRestaurante = new ValidarDadosCriacaoEnderecoRestaurante(
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);
    }

    @Test
    void deveValidarCriacaoComSucesso() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);

        when(validarRestauranteExistente.execute(10L)).thenReturn(restaurante);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(10L, 5L)).thenReturn(false);
        doNothing().when(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);
        doNothing().when(validarCepDuplicado).validarCep(10L, "12345-000");

        Restaurante resultado = validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(validarUsuarioExistente).execute(5L);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(10L, 5L);
        verify(validarRestauranteExistente).execute(10L);
        verify(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);
        verify(validarCepDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioInvalido() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        doThrow(new BadRequestException("usuario.invalido"))
                .when(validarUsuarioExistente).execute(5L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario));
        assertEquals("usuario.invalido", exception.getMessage());
        verify(validarUsuarioExistente).execute(5L);
        verifyNoInteractions(validarEnderecoExistente, validarRestauranteExistente, validarProprietarioRestaurante,
                validarCepDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoRestauranteJaExiste() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(10L, 5L))
                .thenThrow(new BadRequestException("endereco.restaurante.ja.existe"));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario));
        assertEquals("endereco.restaurante.ja.existe", exception.getMessage());
        verify(validarUsuarioExistente).execute(5L);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(10L, 5L);
        verifyNoInteractions(validarRestauranteExistente, validarProprietarioRestaurante, validarCepDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(10L, 5L)).thenReturn(false);
        when(validarRestauranteExistente.execute(10L))
                .thenThrow(new BadRequestException("restaurante.nao.encontrado"));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario));
        assertEquals("restaurante.nao.encontrado", exception.getMessage());
        verify(validarUsuarioExistente).execute(5L);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(10L, 5L);
        verify(validarRestauranteExistente).execute(10L);
        verifyNoInteractions(validarProprietarioRestaurante, validarCepDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoProprietarioDoRestaurante() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);

        when(validarUsuarioExistente.execute(5L)).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(10L, 5L)).thenReturn(false);
        when(validarRestauranteExistente.execute(10L)).thenReturn(restaurante);
        doThrow(new BadRequestException("usuario.nao.proprietario"))
                .when(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario));
        assertEquals("usuario.nao.proprietario", exception.getMessage());
        verify(validarUsuarioExistente).execute(5L);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(10L, 5L);
        verify(validarRestauranteExistente).execute(10L);
        verify(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);
        verifyNoInteractions(validarCepDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");

        Usuario usuario = new Usuario();
        usuario.setId(5L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);

        when(validarUsuarioExistente.execute(5L)).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(10L, 5L)).thenReturn(false);
        when(validarRestauranteExistente.execute(10L)).thenReturn(restaurante);
        doNothing().when(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);
        doThrow(new BadRequestException("cep.ja.cadastrado.para.restaurante"))
                .when(validarCepDuplicado).validarCep(10L, "12345-000");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEnderecoRestaurante.validarCriacao(dto, usuario));
        assertEquals("cep.ja.cadastrado.para.restaurante", exception.getMessage());
        verify(validarUsuarioExistente).execute(5L);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(10L, 5L);
        verify(validarRestauranteExistente).execute(10L);
        verify(validarProprietarioRestaurante).validarProprietario(restaurante, 5L);
        verify(validarCepDuplicado).validarCep(10L, "12345-000");
    }
}