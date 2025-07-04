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
    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Usuário Teste");
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("senha123");
        dto.setLogin("loginTeste");
        dto.setTipo(TipoUsuario.CLIENTE);
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(criarUsuarioCommand.execute(dto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.criarUsuario(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(criarUsuarioCommand).execute(dto);
    }

    @Test
    void deveListarUsuariosComSucesso() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        ListarUsuariosResultadoItem item = new ListarUsuariosResultadoItem();
        item.setId(1L);
        item.setNome("Usuário Teste");
        List<ListarUsuariosResultadoItem> resultado = List.of(item);
        
        when(listarUsuariosQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resultado, response.getBody());
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        when(listarUsuariosQuery.execute(params)).thenReturn(List.of());

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        
        when(atualizarUsuarioComando.execute(id, dto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.atualizarUsuario(id, dto);

        assertEquals(200, response.getStatusCodeValue());
        verify(atualizarUsuarioComando).execute(id, dto);
    }

    @Test
    void deveDesativarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        
        when(desativarUsuarioComando.execute(id)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.desativarUsuario(id);

        assertEquals(200, response.getStatusCodeValue());
        verify(desativarUsuarioComando).execute(id);
    }
} 