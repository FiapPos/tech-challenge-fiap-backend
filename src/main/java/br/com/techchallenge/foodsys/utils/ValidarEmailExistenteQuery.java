package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.domain.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarEmailExistenteQuery {

    private final UsuarioRepository usuarioRepository;

    public void execute(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BadRequestException("email.duplicado");
        }
    }
}