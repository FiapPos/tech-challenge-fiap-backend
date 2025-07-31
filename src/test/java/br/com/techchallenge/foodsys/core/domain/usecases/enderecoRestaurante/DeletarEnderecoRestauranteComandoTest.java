package br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarProprietarioEndereco;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletarEnderecoRestauranteComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;

    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;

    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    @InjectMocks
    private DeletarEnderecoRestauranteComando deletarEnderecoRestauranteComando;

    private Usuario usuario;
    private Endereco endereco;
    private DeletarEnderecoRestauranteComandoDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua das Flores");
        endereco.setCep("12345-678");
        endereco.setNumero("123");
        endereco.setUsuario(usuario);
        
        dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(1L);
    }

    @Test
    void deveDeletarEnderecoRestauranteComSucesso() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> deletarEnderecoRestauranteComando.execute(1L, dto));

        // Assert
        verify(validarUsuarioExistente).execute(1L);
        verify(validarProprietarioEndereco).execute(1L, 1L);
        verify(validarEnderecoExistente).execute(1L);
        verify(enderecoRepository).deleteById(1L);
    }

    @Test
    void deveValidarUsuarioExistenteAntesDeDeletar() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        deletarEnderecoRestauranteComando.execute(1L, dto);

        // Assert
        verify(validarUsuarioExistente).execute(1L);
    }

    @Test
    void deveValidarProprietarioEnderecoAntesDeDeletar() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        deletarEnderecoRestauranteComando.execute(1L, dto);

        // Assert
        verify(validarProprietarioEndereco).execute(1L, 1L);
    }

    @Test
    void deveValidarEnderecoExistenteAntesDeDeletar() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        deletarEnderecoRestauranteComando.execute(1L, dto);

        // Assert
        verify(validarEnderecoExistente).execute(1L);
    }

    @Test
    void deveChamarDeleteByIdComIdCorreto() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        deletarEnderecoRestauranteComando.execute(1L, dto);

        // Assert
        verify(enderecoRepository).deleteById(1L);
    }
} 