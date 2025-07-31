package br.com.techchallenge.foodsys.core.domain.usecases.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.endereco.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.shared.CompartilhadoService;
import br.com.techchallenge.foodsys.core.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizarEnderecoComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;

    @Mock
    private CompartilhadoService sharedService;

    @Mock
    private ValidarCepDoUsuario validarCepDoUsuario;

    @Mock
    private AutorizacaoService autorizacaoService;

    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    @InjectMocks
    private AtualizarEnderecoComando atualizarEnderecoComando;

    private Usuario usuario;
    private Endereco endereco;
    private AtualizarEnderecoComandoDto dto;
    private LocalDateTime dataAtual;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        dataAtual = LocalDateTime.now();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua Antiga");
        endereco.setCep("12345-000");
        endereco.setNumero("100");
        endereco.setUsuario(usuario);
        
        dto = new AtualizarEnderecoComandoDto();
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        // Arrange
        dto.setRua("Nova Rua");
        dto.setCep("54321-987");
        dto.setNumero("200");
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "54321-987");

        // Act
        Endereco resultado = atualizarEnderecoComando.execute(1L, dto, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nova Rua", resultado.getRua());
        assertEquals("54321-987", resultado.getCep());
        assertEquals("200", resultado.getNumero());
        assertEquals(dataAtual, resultado.getDataAtualizacao());
        
        verify(validarProprietarioEndereco).execute(1L, 1L);
        verify(validarEnderecoExistente).execute(1L);
        verify(validarCepDoUsuario).validarCepDuplicado(1L, "54321-987");
        verify(sharedService).getCurrentDateTime();
        verify(enderecoRepository).save(endereco);
    }

    @Test
    void deveAtualizarApenasRua() {
        // Arrange
        dto.setRua("Nova Rua");
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "12345-000");

        // Act
        Endereco resultado = atualizarEnderecoComando.execute(1L, dto, 1L);

        // Assert
        assertEquals("Nova Rua", resultado.getRua());
        assertEquals("12345-000", resultado.getCep());
        assertEquals("100", resultado.getNumero());
    }

    @Test
    void deveAtualizarApenasCep() {
        // Arrange
        dto.setCep("54321-987");
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "54321-987");

        // Act
        Endereco resultado = atualizarEnderecoComando.execute(1L, dto, 1L);

        // Assert
        assertEquals("Rua Antiga", resultado.getRua());
        assertEquals("54321-987", resultado.getCep());
        assertEquals("100", resultado.getNumero());
    }

    @Test
    void deveAtualizarApenasNumero() {
        // Arrange
        dto.setNumero("300");
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).execute(1L, 1L);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "12345-000");

        // Act
        Endereco resultado = atualizarEnderecoComando.execute(1L, dto, 1L);

        // Assert
        assertEquals("Rua Antiga", resultado.getRua());
        assertEquals("12345-000", resultado.getCep());
        assertEquals("300", resultado.getNumero());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            atualizarEnderecoComando.execute(1L, dto, 1L));
        
        verify(validarProprietarioEndereco, never()).execute(any(), any());
        verify(validarEnderecoExistente, never()).execute(any());
    }
} 