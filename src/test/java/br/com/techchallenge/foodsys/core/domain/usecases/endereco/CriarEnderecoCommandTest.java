package br.com.techchallenge.foodsys.core.domain.usecases.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.endereco.CriarEnderecoComandoDto;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.shared.CompartilhadoService;
import br.com.techchallenge.foodsys.core.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.core.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarEnderecoCommandTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private CompartilhadoService sharedService;

    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;

    @Mock
    private ValidarCepDoUsuario validarCepDoUsuario;

    @InjectMocks
    private CriarEnderecoCommand criarEnderecoCommand;

    private Usuario usuario;
    private CriarEnderecoComandoDto dto;
    private Endereco enderecoSalvo;
    private LocalDateTime dataAtual;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        dataAtual = LocalDateTime.now();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        
        dto = new CriarEnderecoComandoDto();
        dto.setRua("Rua das Flores");
        dto.setCep("12345-678");
        dto.setNumero("123");
        
        enderecoSalvo = new Endereco();
        enderecoSalvo.setId(1L);
        enderecoSalvo.setRua("Rua das Flores");
        enderecoSalvo.setCep("12345-678");
        enderecoSalvo.setNumero("123");
        enderecoSalvo.setUsuario(usuario);
        enderecoSalvo.setDataCriacao(dataAtual);
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoSalvo);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "12345-678");

        // Act
        Endereco resultado = criarEnderecoCommand.execute(1L, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rua das Flores", resultado.getRua());
        assertEquals("12345-678", resultado.getCep());
        assertEquals("123", resultado.getNumero());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(dataAtual, resultado.getDataCriacao());
        
        verify(validarUsuarioExistente).execute(1L);
        verify(validarCepDoUsuario).validarCepDuplicado(1L, "12345-678");
        verify(sharedService).getCurrentDateTime();
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveValidarUsuarioExistenteAntesDeCriar() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoSalvo);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "12345-678");

        // Act
        criarEnderecoCommand.execute(1L, dto);

        // Assert
        verify(validarUsuarioExistente).execute(1L);
    }

    @Test
    void deveValidarCepDuplicadoAntesDeCriar() {
        // Arrange
        when(validarUsuarioExistente.execute(1L)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtual);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoSalvo);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(1L, "12345-678");

        // Act
        criarEnderecoCommand.execute(1L, dto);

        // Assert
        verify(validarCepDoUsuario).validarCepDuplicado(1L, "12345-678");
    }
} 