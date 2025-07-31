package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.usuario.AtualizarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.comandos.usuario.DesativarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.query.ListarUsuariosQuery;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private CriarUsuarioCommand criarUsuarioCommand;
    @Mock
    private ListarUsuariosQuery listarUsuariosQuery;
    @Mock
    private AtualizarUsuarioComando atualizarUsuarioComando;
    @Mock
    private DesativarUsuarioComando desativarUsuarioComando;
    @Mock
    private ValidadorPermissoes validadorPermissoes;
    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        CriarUsuarioCommandDto usuarioDto = new CriarUsuarioCommandDto();
        usuarioDto.setNome("Usuário Teste");
        usuarioDto.setEmail("teste@exemplo.com");
        usuarioDto.setSenha("senha123");
        usuarioDto.setLogin("loginTeste");
        usuarioDto.setTipos(List.of(TipoUsuario.CLIENTE));

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(criarUsuarioCommand.execute(usuarioDto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.criarUsuario(usuarioDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(criarUsuarioCommand).execute(usuarioDto);
    }

    @Test
    void deveListarUsuariosComSucesso() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        ListarUsuariosResultadoItem usuarioItem = new ListarUsuariosResultadoItem();
        usuarioItem.setId(1L);
        usuarioItem.setNome("Usuário Teste");
        List<ListarUsuariosResultadoItem> resultado = List.of(usuarioItem);
        
        doNothing().when(validadorPermissoes).validarListagemUsuarios();
        when(listarUsuariosQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(validadorPermissoes).validarListagemUsuarios();
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        doNothing().when(validadorPermissoes).validarListagemUsuarios();
        when(listarUsuariosQuery.execute(params)).thenReturn(List.of());

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(validadorPermissoes).validarListagemUsuarios();
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        AtualizarUsuarioComandoDto usuarioDto = new AtualizarUsuarioComandoDto();
        usuarioDto.setNome("Novo Nome");
        
        doNothing().when(validadorPermissoes).validarGerenciamentoDadosProprios(id);
        when(atualizarUsuarioComando.execute(id, usuarioDto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.atualizarUsuario(id, usuarioDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(validadorPermissoes).validarGerenciamentoDadosProprios(id);
        verify(atualizarUsuarioComando).execute(id, usuarioDto);
    }

    @Test
    void deveDesativarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        
        doNothing().when(validadorPermissoes).validarDesativacaoUsuario();
        when(desativarUsuarioComando.execute(id)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.desativarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(validadorPermissoes).validarDesativacaoUsuario();
        verify(desativarUsuarioComando).execute(id);
    }
}
