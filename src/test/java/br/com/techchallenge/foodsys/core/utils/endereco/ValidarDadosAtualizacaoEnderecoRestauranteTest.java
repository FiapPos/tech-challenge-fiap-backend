package br.com.techchallenge.foodsys.core.utils.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarCepRestauranteDuplicado;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarEnderecoExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarDadosAtualizacaoEnderecoRestauranteTest {

    @Mock
    private ValidarCepRestauranteDuplicado validarCepRestauranteDuplicado;

    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;

    @InjectMocks
    private ValidarDadosAtualizacaoEnderecoRestaurante validarDadosAtualizacaoEnderecoRestaurante;

    private Endereco endereco;
    private Usuario usuario;
    private AtualizarEnderecoRestauranteComandoDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup endereço
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua das Flores");
        endereco.setCep("12345-000");
        endereco.setNumero("123");
        
        // Setup usuário
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        
        // Setup DTO
        dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRestauranteId(10L);
        dto.setCep("12345-000");
        dto.setRua("Nova Rua");
        dto.setNumero("456");
    }

    @Test
    void deveValidarAtualizacaoComSucesso() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Rua das Flores", resultado.getRua());
        assertEquals("12345-000", resultado.getCep());
        assertEquals("123", resultado.getNumero());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveValidarAtualizacaoComCepDiferente() {
        // Arrange
        dto.setCep("54321-000");
        
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "54321-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "54321-000");
    }

    @Test
    void deveValidarAtualizacaoComRestauranteIdDiferente() {
        // Arrange
        dto.setRestauranteId(20L);
        
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(20L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(20L, "12345-000");
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoExiste() {
        // Arrange
        when(validarEnderecoExistente.execute(1L))
            .thenThrow(new BadRequestException("endereco.nao.encontrado"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario));
        
        assertEquals("endereco.nao.encontrado", exception.getMessage());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verifyNoInteractions(validarCepRestauranteDuplicado);
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doThrow(new BadRequestException("cep.duplicado"))
            .when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario));
        
        assertEquals("cep.duplicado", exception.getMessage());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveValidarAtualizacaoComEnderecoIdDiferente() {
        // Arrange
        when(validarEnderecoExistente.execute(5L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(5L, dto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        
        // Verify
        verify(validarEnderecoExistente).execute(5L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveValidarAtualizacaoComUsuarioDiferente() {
        // Arrange
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        outroUsuario.setNome("Maria Santos");
        
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, outroUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveLancarExcecaoQuandoDtoNulo() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, null, usuario));
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verifyNoInteractions(validarCepRestauranteDuplicado);
    }

    @Test
    void deveValidarAtualizacaoComUsuarioNulo() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, null);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveValidarAtualizacaoComEnderecoRetornadoNulo() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(null);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        Endereco resultado = validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Assert
        assertNull(resultado);
        
        // Verify
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }

    @Test
    void deveChamarValidarEnderecoExistenteComIdCorreto() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Verify
        verify(validarEnderecoExistente).execute(1L);
    }

    @Test
    void deveChamarValidarCepRestauranteDuplicadoComParametrosCorretos() {
        // Arrange
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");

        // Act
        validarDadosAtualizacaoEnderecoRestaurante.validarAtualizacao(1L, dto, usuario);

        // Verify
        verify(validarCepRestauranteDuplicado).validarCep(10L, "12345-000");
    }
} 