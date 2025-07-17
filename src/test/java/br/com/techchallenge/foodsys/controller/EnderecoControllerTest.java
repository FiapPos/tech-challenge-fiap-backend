package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
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
    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(1L);
        
        doNothing().when(autorizacaoService).validarAcessoUsuario(dto.getUsuarioId());
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        when(criarEnderecoCommand.execute(dto)).thenReturn(endereco);

        ResponseEntity<Void> response = enderecoController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(dto.getUsuarioId());
        verify(criarEnderecoCommand).execute(dto);
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        Long id = 1L;
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setUsuarioId(1L);
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setUsuario(usuario);
        when(enderecoRepository.findById(id)).thenReturn(Optional.of(endereco));
        
        doNothing().when(autorizacaoService).validarAcessoUsuario(dto.getUsuarioId());
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setId(id);
        when(atualizarEnderecoComando.execute(id, dto)).thenReturn(enderecoAtualizado);

        ResponseEntity<Void> response = enderecoController.atualizar(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(dto.getUsuarioId());
        verify(atualizarEnderecoComando).execute(id, dto);
        verify(enderecoRepository).findById(id);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(1L);
        dto.setUsuarioId(1L);
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Endereco endereco = new Endereco();
        endereco.setId(dto.getEnderecoId());
        endereco.setUsuario(usuario);
        when(enderecoRepository.findById(dto.getEnderecoId())).thenReturn(Optional.of(endereco));
        
        doNothing().when(autorizacaoService).validarAcessoUsuario(usuario.getId());
        doNothing().when(deletarEnderecoComando).execute(dto);

        ResponseEntity<Void> response = enderecoController.deletar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).validarAcessoUsuario(usuario.getId());
        verify(deletarEnderecoComando).execute(dto);
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
        
        doNothing().when(autorizacaoService).validarAcessoUsuario(usuarioId);
        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(resultado);

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController.listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        Long usuarioId = 1L;
        
        doNothing().when(autorizacaoService).validarAcessoUsuario(usuarioId);
        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(List.of());

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController.listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        Long id = 1L;
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(1L);
        
        when(enderecoRepository.findById(id)).thenReturn(Optional.empty());
        
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> enderecoController.atualizar(id, dto)
        );
        
        assertEquals("endereco.nao.encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoUsuario() {
        Long id = 1L;
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(2L); // Usuário diferente
        
        Usuario usuario = new Usuario();
        usuario.setId(1L); // Usuário do endereço
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setUsuario(usuario);
        when(enderecoRepository.findById(id)).thenReturn(Optional.of(endereco));
        
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> enderecoController.atualizar(id, dto)
        );
        
        assertEquals("endereco.nao.pertence.ao.usuario", exception.getMessage());
    }
} 