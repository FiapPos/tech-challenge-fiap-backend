package br.com.techchallenge.foodsys.core.queries;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.queries.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.core.queries.tipo.ListarPorTipoUsuario;
import br.com.techchallenge.foodsys.core.queries.tipo.TipoUsuarioResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarUsuariosQueryTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListarPorTipoUsuario listarPorTipoUsuario;

    @InjectMocks
    private ListarUsuariosQuery listarUsuariosQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosUsuariosQuandoAtivoForNull() {
        Usuario usuario = criarUsuario(1L, true);

        List<TipoUsuarioResultItem> tiposEsperados = List.of(
            TipoUsuarioResultItem.builder()
                .tipo(TipoUsuario.CLIENTE)
                .build()
        );
        when(listarPorTipoUsuario.execute(any(Set.class))).thenReturn(tiposEsperados);
        when(usuarioRepository.findAll(any(Sort.class))).thenReturn(List.of(usuario));

        ListarUsuariosParams params = new ListarUsuariosParams(null);
        List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.getFirst().getId());
        assertEquals(usuario.getNome(), resultado.getFirst().getNome());
        assertEquals(usuario.getEmail(), resultado.getFirst().getEmail());
        assertEquals(usuario.getLogin(), resultado.getFirst().getLogin());
        assertEquals(tiposEsperados, resultado.getFirst().getTipo());
        assertEquals(usuario.getDataCriacao(), resultado.getFirst().getDataCriacao());
        assertEquals(usuario.getDataAtualizacao(), resultado.getFirst().getDataAtualizacao());

        verify(listarPorTipoUsuario, times(1)).execute(usuario.getUsuarioTipos());
    }

    @Test
    void deveListarUsuariosPorAtivo() {
        Usuario usuario = criarUsuario(2L, false);

        List<TipoUsuarioResultItem> tiposEsperados = List.of(
            TipoUsuarioResultItem.builder()
                .tipo(TipoUsuario.CLIENTE)
                .build()
        );
        when(listarPorTipoUsuario.execute(any(Set.class))).thenReturn(tiposEsperados);
        when(usuarioRepository.findByAtivo(eq(false), any(Sort.class))).thenReturn(List.of(usuario));

        ListarUsuariosParams params = new ListarUsuariosParams(false);
        List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.getFirst().getId());
        assertEquals(usuario.getNome(), resultado.getFirst().getNome());
        assertEquals(usuario.getEmail(), resultado.getFirst().getEmail());
        assertEquals(usuario.getLogin(), resultado.getFirst().getLogin());
        assertEquals(tiposEsperados, resultado.getFirst().getTipo());
        assertEquals(usuario.getDataCriacao(), resultado.getFirst().getDataCriacao());
        assertEquals(usuario.getDataAtualizacao(), resultado.getFirst().getDataAtualizacao());

        verify(listarPorTipoUsuario, times(1)).execute(usuario.getUsuarioTipos());
    }

    private Usuario criarUsuario(Long id, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senha");
        usuario.setLogin("loginTeste");

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        usuario.setAtivo(ativo);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());
        return usuario;
    }
}
