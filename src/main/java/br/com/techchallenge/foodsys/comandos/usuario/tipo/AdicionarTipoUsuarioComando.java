package br.com.techchallenge.foodsys.comandos.usuario.tipo;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioTipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.utils.usuario.tipo.VerificarTipoUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdicionarTipoUsuarioComando {
    private final VerificarTipoUsuarioExistente verificarTipoExistente;

    public void execute(Usuario usuario, TipoUsuario tipo) {
        verificarTipoExistente.execute(usuario, tipo);
        criarEAdicionarTipo(usuario, tipo);
    }

    private void criarEAdicionarTipo(Usuario usuario, TipoUsuario tipo) {
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(tipo);
        usuario.getUsuarioTipos().add(usuarioTipo);
    }
}
