package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoUsuarioCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoUsuarioComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoControllerTest {
    @Mock
    private CriarEnderecoCommand criarEnderecoCommand;
    @Mock
    private AtualizarEnderecoComando atualizarEnderecoComando;
    @Mock
    private DeletarEnderecoComando deletarEnderecoComando;
    @Mock
    private ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;
    @Mock
    private AutorizacaoService autorizacaoService;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidadorPermissoes validadorPermissoes;
    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        CriarEnderecoUsuarioCommandDto dto = new CriarEnderecoUsuarioCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(1L);
        
        doNothing().when(validadorPermissoes).validarGerenciamentoDadosProprios(dto.getUsuarioId());
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        when(criarEnderecoCommand.execute(dto.getUsuarioId(), dto)).thenReturn(endereco);

        ResponseEntity<Void> response = enderecoController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(validadorPermissoes).validarGerenciamentoDadosProprios(dto.getUsuarioId());
        verify(criarEnderecoCommand).execute(dto.getUsuarioId(), dto);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        DeletarEnderecoUsuarioComandoDto dto = new DeletarEnderecoUsuarioComandoDto();
        dto.setEnderecoId(1L);
        dto.setUsuarioId(1L);
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Endereco endereco = new Endereco();
        endereco.setId(dto.getEnderecoId());
        endereco.setUsuario(usuario);
        when(enderecoRepository.findById(dto.getEnderecoId())).thenReturn(Optional.of(endereco));
        
        doNothing().when(validadorPermissoes).validarGerenciamentoDadosProprios(usuario.getId());
        doNothing().when(deletarEnderecoComando).execute(dto.getUsuarioId(), dto);

        ResponseEntity<Void> response = enderecoController.deletar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(validadorPermissoes).validarGerenciamentoDadosProprios(usuario.getId());
        verify(deletarEnderecoComando).execute(dto.getUsuarioId(), dto);
        verify(enderecoRepository).findById(dto.getEnderecoId());
    }

    @Test
    void deveListarEnderecosPorUsuarioComSucesso() {
        Long usuarioId = 1L;
        ListarEnderecoPorIdUsuarioResultadoItem item = ListarEnderecoPorIdUsuarioResultadoItem.builder()
                .id(1L)
                .rua("Rua Teste")
                .build();
        List<ListarEnderecoPorIdUsuarioResultadoItem> resultado = List.of(item);
        
        doNothing().when(validadorPermissoes).validarGerenciamentoDadosProprios(usuarioId);
        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(resultado);

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController.listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(validadorPermissoes).validarGerenciamentoDadosProprios(usuarioId);
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        Long usuarioId = 1L;
        
        doNothing().when(validadorPermissoes).validarGerenciamentoDadosProprios(usuarioId);
        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(List.of());

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController.listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(validadorPermissoes).validarGerenciamentoDadosProprios(usuarioId);
    }

}
