package br.com.techchallenge.foodsys.utils;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;

@RequiredArgsConstructor
@Service

public class ValidarRestauranteExistente {

    private final RestauranteRepository restauranteRepository;

    public Restaurante execute(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("restaurante.nao.encontrado"));
    }

    public void validarNomeRestauranteDuplicado(String nome) {
        if (restauranteRepository.findRestauranteByNome(nome) != null) {
            throw new BadRequestException("restaurante.duplicado");
        }
    }
}
