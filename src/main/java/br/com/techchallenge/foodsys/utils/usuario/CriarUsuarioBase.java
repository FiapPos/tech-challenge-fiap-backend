package br.com.techchallenge.foodsys.utils.usuario;
import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioBase {
    private final PasswordEncoder passwordEncoder;
    private final CompartilhadoService sharedService;

    public Usuario execute(CriarUsuarioCommandDto dto) {
        Usuario usuario = new Usuario();
        configurarDadosBasicos(usuario, dto);
        configurarDadosSeguranca(usuario, dto);

        return usuario;
    }

    private void configurarDadosBasicos(Usuario usuario, CriarUsuarioCommandDto dto) {
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
    }

    private void configurarDadosSeguranca(Usuario usuario, CriarUsuarioCommandDto dto) {
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCriacao(sharedService.getCurrentDateTime());
        usuario.setAtivo(true);
    }
}

