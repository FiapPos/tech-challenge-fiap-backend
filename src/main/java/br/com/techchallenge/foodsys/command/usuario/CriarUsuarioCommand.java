package br.com.techchallenge.foodsys.command.usuario;

import br.com.techchallenge.foodsys.command.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.domain.usuario.Usuario;
import br.com.techchallenge.foodsys.domain.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.shared.SharedService;
import br.com.techchallenge.foodsys.utils.ValidarEmailExistenteQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioCommand {

    private final UsuarioRepository usuarioRepository;
    private final SharedService sharedService;
    private final ValidarEmailExistenteQuery validarEmailExistenteQuery;

    public Usuario execute(CriarUsuarioCommandDto criarUsuarioCommandDto) {
        validarEmailExistenteQuery.execute(criarUsuarioCommandDto.getEmail());
        Usuario usuario = mapToEntity(criarUsuarioCommandDto);
        return usuarioRepository.save(usuario);
    }

    private Usuario mapToEntity(CriarUsuarioCommandDto criarUsuarioCommandDto) {
        Usuario usuario = new Usuario();
        usuario.setNome(criarUsuarioCommandDto.getNome());
        usuario.setEmail(criarUsuarioCommandDto.getEmail());
        usuario.setSenha(criarUsuarioCommandDto.getSenha().getBytes());
        usuario.setLogin(criarUsuarioCommandDto.getLogin());
        usuario.setTipo(TipoUsuario.fromCodigo(criarUsuarioCommandDto.getTipo().getCodigo()));
        usuario.setDataCriacao(sharedService.getCurrentDateTime());
        return usuario;
    }

}

