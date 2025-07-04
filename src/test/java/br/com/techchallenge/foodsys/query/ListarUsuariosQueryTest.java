package br.com.techchallenge.foodsys.query;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarUsuariosQueryTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ListarUsuariosQuery listarUsuariosQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosUsuariosQuandoAtivoForNull() {
        Usuario usuario = criarUsuario(1L, true);
        when(usuarioRepository.findAll(any(Sort.class))).thenReturn(List.of(usuario));

        ListarUsuariosParams params = new ListarUsuariosParams(null);
        List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.getFirst().getId());
        assertEquals(usuario.getNome(), resultado.getFirst().getNome());
        assertEquals(usuario.getEmail(), resultado.getFirst().getEmail());
        assertEquals(usuario.getLogin(), resultado.getFirst().getLogin());
        assertEquals(usuario.getTipo(), resultado.getFirst().getTipo());
        assertEquals(usuario.getDataCriacao(), resultado.getFirst().getDataCriacao());
        assertEquals(usuario.getDataAtualizacao(), resultado.getFirst().getDataAtualizacao());
    }

    @Test
    void deveListarUsuariosPorAtivo() {
        Usuario usuario = criarUsuario(2L, false);
        when(usuarioRepository.findByAtivo(eq(false), any(Sort.class))).thenReturn(List.of(usuario));

        ListarUsuariosParams params = new ListarUsuariosParams(false);
        List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.getFirst().getId());
        assertEquals(usuario.getNome(), resultado.getFirst().getNome());
        assertEquals(usuario.getEmail(), resultado.getFirst().getEmail());
        assertEquals(usuario.getLogin(), resultado.getFirst().getLogin());
        assertEquals(usuario.getTipo(), resultado.getFirst().getTipo());
        assertEquals(usuario.getDataCriacao(), resultado.getFirst().getDataCriacao());
        assertEquals(usuario.getDataAtualizacao(), resultado.getFirst().getDataAtualizacao());
    }

    private Usuario criarUsuario(Long id, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senha");
        usuario.setLogin("loginTeste");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(ativo);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());
        return usuario;
    }
} 