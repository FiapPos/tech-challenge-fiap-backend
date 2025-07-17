package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCepDoUsuario {

    private final EnderecoRepository enderecoRepository;

    public void validarCepDuplicado(Long usuarioId, String cep) {
        if (enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep)) {
            throw new BadRequestException("endereco.cep.duplicado");
        }
    }
}
