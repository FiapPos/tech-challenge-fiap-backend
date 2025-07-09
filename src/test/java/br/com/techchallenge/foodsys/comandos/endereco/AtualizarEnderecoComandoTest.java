package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
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
    @InjectMocks
    private AtualizarEnderecoComando atualizarEnderecoComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAtualizarEnderecoComCamposCorretos() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(2L);

        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco atualizado = atualizarEnderecoComando.execute(id, dto);
        assertEquals("Nova Rua", atualizado.getRua());
        assertEquals("12345-678", atualizado.getCep());
        assertEquals("100", atualizado.getNumero());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(2L);
        assertThrows(BadRequestException.class, () -> atualizarEnderecoComando.execute(1L, dto));
    }

    @Test
    void deveValidarCepDuplicado() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setCep("12345-678");
        dto.setUsuarioId(2L);
        dto.setRua("Rua Teste");

        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        atualizarEnderecoComando.execute(id, dto);
        verify(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
    }

    @Test
    void deveAtualizarApenasRuaQuandoApenasRuaPreenchida() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");
        
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setUsuarioId(2L);
        
        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco atualizado = atualizarEnderecoComando.execute(id, dto);
        
        assertEquals("Nova Rua", atualizado.getRua());
        assertEquals("00000-000", atualizado.getCep());
        assertEquals("50", atualizado.getNumero());
    }

    @Test
    void deveAtualizarApenasCepQuandoApenasCepPreenchido() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");
        
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setCep("54321-987");
        dto.setUsuarioId(2L);
        
        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco atualizado = atualizarEnderecoComando.execute(id, dto);
        
        assertEquals("Rua Antiga", atualizado.getRua());
        assertEquals("54321-987", atualizado.getCep());
        assertEquals("50", atualizado.getNumero());
    }

    @Test
    void deveAtualizarApenasNumeroQuandoApenasNumeroPreenchido() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");
        
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setNumero("200");
        dto.setUsuarioId(2L);
        
        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco atualizado = atualizarEnderecoComando.execute(id, dto);
        
        assertEquals("Rua Antiga", atualizado.getRua());
        assertEquals("00000-000", atualizado.getCep());
        assertEquals("200", atualizado.getNumero());
    }

    @Test
    void deveAtualizarRuaECepQuandoApenasEssesCamposPreenchidos() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setRua("Rua Antiga");
        endereco.setCep("00000-000");
        endereco.setNumero("50");
        
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep("54321-987");
        dto.setUsuarioId(2L);
        
        when(validarEnderecoExistente.execute(id)).thenReturn(endereco);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(dto.getUsuarioId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco atualizado = atualizarEnderecoComando.execute(id, dto);
        
        assertEquals("Nova Rua", atualizado.getRua());
        assertEquals("54321-987", atualizado.getCep());
        assertEquals("50", atualizado.getNumero());
    }

    @Test
    void deveLancarExcecaoQuandoTodosCamposNulos() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(2L);
        
        assertThrows(BadRequestException.class, () -> atualizarEnderecoComando.execute(1L, dto));
    }
} 