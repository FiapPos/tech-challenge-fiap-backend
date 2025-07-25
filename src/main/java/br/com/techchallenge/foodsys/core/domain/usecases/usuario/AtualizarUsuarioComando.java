package br.com.techchallenge.foodsys.core.domain.usecases.usuario;

import br.com.techchallenge.foodsys.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.shared.CompartilhadoService;
import br.com.techchallenge.foodsys.core.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarLoginExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarUsuarioComando {

    private final UsuarioRepository usuarioRepository;
    private final ValidarEmailExistente validarEmailExistente;
    private final ValidarLoginExistente validarLoginExistente;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final CompartilhadoService sharedService;
    private final PasswordEncoder passwordEncoder;

    public Usuario execute(Long id, AtualizarUsuarioComandoDto dto) {
        validarDto(dto);
        Usuario usuario = validarUsuarioExistente.execute(id);
        validarCamposUnicos(usuario, dto);
        atualizarCampos(usuario, dto);
        return usuarioRepository.save(usuario);
    }

    private void validarDto(AtualizarUsuarioComandoDto dto) {
        if (!isPeloMenosUmCampoPreenchido(dto)) {
            throw new BadRequestException("atualizar.usuario.nenhum.campo");
        }
    }

    private void validarCamposUnicos(Usuario usuario, AtualizarUsuarioComandoDto dto) {
        validarEmailUnico(usuario, dto);
        validarLoginUnico(usuario, dto);
    }

    private void validarEmailUnico(Usuario usuario, AtualizarUsuarioComandoDto dto) {
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            validarEmailExistente.execute(dto.getEmail());
        }
    }

    private void validarLoginUnico(Usuario usuario, AtualizarUsuarioComandoDto dto) {
        if (dto.getLogin() != null && !dto.getLogin().equals(usuario.getLogin())) {
            validarLoginExistente.execute(dto.getLogin());
        }
    }

    private boolean isPeloMenosUmCampoPreenchido(AtualizarUsuarioComandoDto dto) {
        return dto.getNome() != null || dto.getEmail() != null || dto.getSenha() != null ||
                dto.getLogin() != null;
    }

    private void atualizarCampos(Usuario usuario, AtualizarUsuarioComandoDto dto) {
        atualizarNome(usuario, dto.getNome());
        atualizarEmail(usuario, dto.getEmail());
        atualizarLogin(usuario, dto.getLogin());
        atualizarSenha(usuario, dto.getSenha());
        usuario.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    private void atualizarNome(Usuario usuario, String nome) {
        if (nome != null) {
            usuario.setNome(nome);
        }
    }

    private void atualizarEmail(Usuario usuario, String email) {
        if (email != null) {
            usuario.setEmail(email);
        }
    }

    private void atualizarLogin(Usuario usuario, String login) {
        if (login != null) {
            usuario.setLogin(login);
        }
    }

    private void atualizarSenha(Usuario usuario, String senha) {
        if (senha != null && !senha.isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(senha));
        }
    }
}
