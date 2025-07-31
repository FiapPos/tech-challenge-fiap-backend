package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
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
