package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoComCamposCorretos() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        LocalDateTime dataCriacao = LocalDateTime.now();
        when(validarUsuarioExistente.execute(dto.getUsuarioId())).thenReturn(usuario);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(usuario.getId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(dataCriacao);
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        Endereco endereco = criarEnderecoCommand.execute(dto);
        assertEquals(dto.getRua(), endereco.getRua());
        assertEquals(dto.getCep(), endereco.getCep());
        assertEquals(dto.getNumero(), endereco.getNumero());
        assertEquals(usuario, endereco.getUsuario());
        assertEquals(dataCriacao, endereco.getDataCriacao());
    }

    @Test
    void deveValidarUsuarioECepDuplicado() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(validarUsuarioExistente.execute(dto.getUsuarioId())).thenReturn(usuario);
        doNothing().when(validarCepDoUsuario).validarCepDuplicado(usuario.getId(), dto.getCep());
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

        criarEnderecoCommand.execute(dto);
        verify(validarUsuarioExistente).execute(dto.getUsuarioId());
        verify(validarCepDoUsuario).validarCepDuplicado(usuario.getId(), dto.getCep());
    }
} 