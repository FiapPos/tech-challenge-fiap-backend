package br.com.techchallenge.foodsys.infrastructure.services;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarProprietarioEnderecoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    private Usuario usuario;
    private Endereco endereco;

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
    }

    @Test
    void deveValidarProprietarioComSucesso() {
        // Arrange
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Act
        assertDoesNotThrow(() -> validarProprietarioEndereco.execute(1L, 1L));

        // Assert
        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        // Arrange
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            validarProprietarioEndereco.execute(1L, 1L));
        
        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoUsuario() {
        // Arrange
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            validarProprietarioEndereco.execute(1L, 2L));
        
        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveValidarProprietarioComUsuarioDiferente() {
        // Arrange
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        endereco.setUsuario(outroUsuario);
        
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Act
        assertDoesNotThrow(() -> validarProprietarioEndereco.execute(1L, 2L));

        // Assert
        verify(enderecoRepository).findById(1L);
    }
} 