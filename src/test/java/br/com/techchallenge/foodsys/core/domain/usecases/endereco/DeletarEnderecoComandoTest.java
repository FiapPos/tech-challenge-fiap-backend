package br.com.techchallenge.foodsys.core.domain.usecases.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.endereco.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletarEnderecoComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;

    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;

    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    @InjectMocks
    private DeletarEnderecoComando deletarEnderecoComando;

    private Usuario usuario;
    private Endereco endereco;
    private DeletarEnderecoComandoDto dto;

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
        
        dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(1L);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(enderecoRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> deletarEnderecoComando.execute(1L, dto));

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
        deletarEnderecoComando.execute(1L, dto);

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
        deletarEnderecoComando.execute(1L, dto);

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
        deletarEnderecoComando.execute(1L, dto);

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
        deletarEnderecoComando.execute(1L, dto);

        // Assert
        verify(enderecoRepository).deleteById(1L);
    }
} 