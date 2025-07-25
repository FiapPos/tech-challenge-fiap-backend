package br.com.techchallenge.foodsys.core.domain.usecases.usuario;

import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarLoginExistente;
import br.com.techchallenge.foodsys.core.utils.usuario.CriarUsuarioBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioCommand {

    private final UsuarioRepository usuarioRepository;
    private final ValidarEmailExistente validarEmailExistente;
    private final ValidarLoginExistente validarLoginExistente;
    private final CriarUsuarioBase criarUsuarioBase;
    private final AdicionarTipoUsuarioComando adicionarTipoUsuarioComando;

    public Usuario execute(CriarUsuarioCommandDto dto) {
        validarDadosUsuario(dto);
        Usuario usuario = criarUsuarioBase.execute(dto);
        adicionarTipoUsuario(usuario, dto);
        return salvarUsuario(usuario);
    }

    private void validarDadosUsuario(CriarUsuarioCommandDto dto) {
        validarEmailExistente.execute(dto.getEmail());
        validarLoginExistente.execute(dto.getLogin());
    }

    private void adicionarTipoUsuario(Usuario usuario, CriarUsuarioCommandDto dto) {
        dto.getTipos().forEach(tipo -> {
            adicionarTipoUsuarioComando.execute(usuario, tipo);
        });
    }

    private Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}