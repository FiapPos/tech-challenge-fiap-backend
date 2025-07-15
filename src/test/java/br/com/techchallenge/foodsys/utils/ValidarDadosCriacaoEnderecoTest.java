package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarDadosCriacaoEnderecoTest {

    @Mock
    private ValidarCepDuplicado validarCepDuplicado;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private ValidarProprietarioRestaurante validarProprietarioRestaurante;

    private ValidarDadosCriacaoEndereco validarDadosCriacaoEndereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validarDadosCriacaoEndereco = new ValidarDadosCriacaoEndereco(
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);
    }

    @Test
    void deveValidarUsuarioExistente() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);

        validarDadosCriacaoEndereco.validarCriacao(dto, usuario);

        verify(validarUsuarioExistente).execute(1L);
    }

    @Test
    void deveValidarRestauranteExistenteQuandoRestauranteIdInformado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarRestauranteExistente.execute(2L)).thenReturn(restaurante);

        validarDadosCriacaoEndereco.validarCriacao(dto, usuario);

        verify(validarRestauranteExistente).execute(2L);
    }

    @Test
    void deveValidarProprietarioRestauranteQuandoRestauranteIdInformado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarRestauranteExistente.execute(2L)).thenReturn(restaurante);

        validarDadosCriacaoEndereco.validarCriacao(dto, usuario);

        verify(validarProprietarioRestaurante).validarProprietario(restaurante, 1L);
    }

    @Test
    void deveValidarEnderecoRestauranteExistenteQuandoRestauranteIdInformado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);

        validarDadosCriacaoEndereco.validarCriacao(dto, usuario);

        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(2L, 1L);
    }

    @Test
    void deveValidarCepDuplicado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);

        validarDadosCriacaoEndereco.validarCriacao(dto, usuario);

        verify(validarCepDuplicado).validarCep(1L, 2L, "12345-000");
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        doThrow(new BadRequestException("cep.duplicado"))
                .when(validarCepDuplicado).validarCep(1L, 2L, "12345-000");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEndereco.validarCriacao(dto, usuario));
        assertEquals("cep.duplicado", exception.getMessage());

        verify(validarUsuarioExistente).execute(1L);
        verify(validarCepDuplicado).validarCep(1L, 2L, "12345-000");
        verify(validarEnderecoExistente, atLeast(0)).validarEnderecoRestauranteExistente(2L, 1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        Long usuarioId = 1L;

        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        ValidarUsuarioExistente validarUsuarioExistente = new ValidarUsuarioExistente(usuarioRepository);

        when(usuarioRepository.findById(usuarioId)).thenReturn(java.util.Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarUsuarioExistente.execute(usuarioId));
        assertEquals("usuario.nao.encontrado", exception.getMessage());

        verify(usuarioRepository).findById(usuarioId);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontradoParaRestaurante() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        doThrow(new BadRequestException("endereco.restaurante.nao.encontrado"))
                .when(validarEnderecoExistente).validarEnderecoRestauranteExistente(2L, 1L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEndereco.validarCriacao(dto, usuario));
        assertEquals("endereco.restaurante.nao.encontrado", exception.getMessage());

        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(2L, 1L);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(2L, 1L)).thenReturn(false);
        doThrow(new BadRequestException("restaurante.nao.encontrado"))
                .when(validarRestauranteExistente).execute(2L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEndereco.validarCriacao(dto, usuario));
        assertEquals("restaurante.nao.encontrado", exception.getMessage());

        verify(validarRestauranteExistente).execute(2L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoProprietarioDoRestaurante() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(2L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);

        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(2L, 1L)).thenReturn(false);
        when(validarRestauranteExistente.execute(2L)).thenReturn(restaurante);
        doThrow(new BadRequestException("usuario.nao.proprietario"))
                .when(validarProprietarioRestaurante).validarProprietario(restaurante, 1L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosCriacaoEndereco.validarCriacao(dto, usuario));
        assertEquals("usuario.nao.proprietario", exception.getMessage());

        verify(validarProprietarioRestaurante).validarProprietario(restaurante, 1L);
    }
}