package br.com.techchallenge.foodsys.core.queries;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.queries.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.core.queries.tipo.ListarPorTipoUsuario;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarUsuariosQuery {

    private final UsuarioRepository usuarioRepository;
    private final ListarPorTipoUsuario listarPorTipoUsuario;

    public List<ListarUsuariosResultadoItem> execute(ListarUsuariosParams params) {
        List<Usuario> usuarios = buscarUsuarios(params);
        return mapToResultadoItemList(usuarios);
    }

    private List<ListarUsuariosResultadoItem> mapToResultadoItemList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarUsuariosResultadoItem mapToResultadoItem(Usuario usuario) {
        return ListarUsuariosResultadoItem.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipo(listarPorTipoUsuario.execute(usuario.getUsuarioTipos()))
                .dataCriacao(usuario.getDataCriacao())
                .dataAtualizacao(usuario.getDataAtualizacao())
                .build();
    }

    private List<Usuario> buscarUsuarios(ListarUsuariosParams params) {
        if (params.getAtivo() == null) {
            return usuarioRepository.findAll();
        }
        return usuarioRepository.findByAtivo(params.getAtivo());
    }
}