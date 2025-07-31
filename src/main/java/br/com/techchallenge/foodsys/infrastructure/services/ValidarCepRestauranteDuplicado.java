package br.com.techchallenge.foodsys.infrastructure.services;

import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
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