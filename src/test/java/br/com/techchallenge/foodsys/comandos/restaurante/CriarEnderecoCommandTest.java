package br.com.techchallenge.foodsys.comandos.restaurante;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarCepDuplicado;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioRestaurante;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

public class CriarEnderecoCommandTest {

    @InjectMocks
    private CriarEnderecoCommand criarEnderecoComand;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private CompartilhadoService sharedService;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoParaRestaurante() {
        // Arrange
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

        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        // Mocks
        when(validarUsuarioExistente.execute(usuario.getId())).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId)).thenReturn(false);
        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);
        doNothing().when(validarProprietarioRestaurante).validarProprietario(restaurante, usuarioId);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, restauranteId, dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(java.time.LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act
        Endereco resultado = comando.execute(dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(dto.getRua(), resultado.getRua());
        assertEquals(dto.getCep(), resultado.getCep());
        assertEquals(dto.getNumero(), resultado.getNumero());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertEquals(restauranteId, resultado.getRestaurante().getId());
        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarEnderecoExistente).validarEnderecoRestauranteExistente(restauranteId, usuarioId);
        verify(validarRestauranteExistente).execute(restauranteId);
        verify(validarProprietarioRestaurante).validarProprietario(restaurante, usuarioId);
        verify(validarCepDuplicado).validarCep(usuarioId, restauranteId, dto.getCep());
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveCriarEnderecoParaUsuario() {
        // Arrange
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Usuário");
        dto.setCep("98765-432");
        dto.setNumero("20");

        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setUsuario(usuario);

        // Mocks
        when(validarUsuarioExistente.execute(usuario.getId())).thenReturn(usuario);
        doNothing().when(validarCepDuplicado).validarCep(usuarioId, null, dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(java.time.LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act
        Endereco resultado = comando.execute(dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(dto.getRua(), resultado.getRua());
        assertEquals(dto.getCep(), resultado.getCep());
        assertEquals(dto.getNumero(), resultado.getNumero());
        assertEquals(usuarioId, resultado.getUsuario().getId());
        assertNull(resultado.getRestaurante());
        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarCepDuplicado).validarCep(usuarioId, null, dto.getCep());
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-000");
        dto.setNumero("10");

        // Mock: usuário não existe, lança exceção
        doThrow(new BadRequestException("usuario.nao.encontrado"))
                .when(validarUsuarioExistente).execute(usuarioId);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(dto, usuario));
        assertEquals("usuario.nao.encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoDoRestauranteExiste() {
        // Arrange
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        doThrow(new BadRequestException("endereco.restaurante.ja.existe"))
                .when(validarEnderecoExistente).validarEnderecoRestauranteExistente(restauranteId, usuarioId);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(dto, usuario));
        assertEquals("endereco.restaurante.ja.existe", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        // Arrange
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        when(validarUsuarioExistente.execute(usuario.getId())).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId)).thenReturn(false);
        doThrow(new BadRequestException("restaurante.nao.encontrado"))
                .when(validarRestauranteExistente).execute(restauranteId);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(dto, usuario));
        assertEquals("restaurante.nao.encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForProprietarioRestaurante() {
        // Arrange
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Endereco endereco = new Endereco();
        endereco.setId(100L);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        // Mocks
        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId)).thenReturn(false);
        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);
        doThrow(new BadRequestException("usuario.nao.e.proprietario.restaurante"))
                .when(validarProprietarioRestaurante).validarProprietario(restaurante, usuarioId);

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(dto, usuario));
        assertEquals("usuario.nao.e.proprietario.restaurante", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        // Arrange
        Long usuarioId = 1L;
        Long restauranteId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-000");
        dto.setNumero("10");
        dto.setRestauranteId(restauranteId);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Endereco endereco = new Endereco();
        endereco.setId(100L);
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        // Mocks
        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(validarEnderecoExistente.validarEnderecoRestauranteExistente(restauranteId, usuarioId))
                .thenReturn(false);
        when(validarRestauranteExistente.execute(restauranteId)).thenReturn(restaurante);
        doNothing().when(validarProprietarioRestaurante).validarProprietario(restaurante, usuarioId);
        doThrow(new BadRequestException("cep.duplicado"))
                .when(validarCepDuplicado).validarCep(usuarioId, restauranteId, dto.getCep());

        CriarEnderecoCommand comando = new CriarEnderecoCommand(
                enderecoRepository,
                sharedService,
                validarCepDuplicado,
                validarUsuarioExistente,
                validarEnderecoExistente,
                validarRestauranteExistente,
                validarProprietarioRestaurante);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(dto, usuario));
        assertEquals("cep.duplicado", exception.getMessage());
    }
}
