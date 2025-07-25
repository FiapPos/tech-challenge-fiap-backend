package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCepRestauranteDuplicado {

    private final EnderecoRepository enderecoRepository;

    public void validarCep(Long restauranteId, String cep) {

        boolean existe = enderecoRepository.existsByRestauranteIdAndCep(restauranteId, cep);
        if (existe) {
            throw new BadRequestException("cep.ja.cadastrado.para.restaurante");
        }
    }
}