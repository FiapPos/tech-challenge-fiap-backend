package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarLoginExistente {
    private final UsuarioRepository usuarioRepository;

    public void execute(String login) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new BadRequestException("login.duplicado");
        }
    }
}
