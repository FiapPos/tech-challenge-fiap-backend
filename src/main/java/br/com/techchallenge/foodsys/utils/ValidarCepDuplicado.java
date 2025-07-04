package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCepDuplicado {

    private final EnderecoRepository enderecoRepository;

    public void validarCep(Long usuarioId, Long restauranteId, String cep) {

        if (restauranteId != null) {
            boolean existe = enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep);
            if (existe) {
                throw new BadRequestException("cep.ja.cadastrado.para.restaurante");
            }
        } else {
            boolean existe = enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep);
            if (existe) {
                throw new BadRequestException("cep.ja.cadastrado.para.usuario");
            }
        }
    }
}
