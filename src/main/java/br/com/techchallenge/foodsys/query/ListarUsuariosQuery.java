package br.com.techchallenge.foodsys.query;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.query.tipo.ListarPorTipoUsuario;
import br.com.techchallenge.foodsys.query.tipo.TipoUsuarioResultItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (params.getAtivo() == null) {
            return usuarioRepository.findAll(sort);
        }
        return usuarioRepository.findByAtivo(params.getAtivo(), sort);
    }
}