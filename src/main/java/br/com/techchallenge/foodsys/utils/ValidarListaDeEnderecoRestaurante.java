package br.com.techchallenge.foodsys.utils;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

@Service
@RequiredArgsConstructor

public class ValidarListaDeEnderecoRestaurante {

    private final EnderecoRepository enderecoRepository;
    private final ValidarRestauranteExistente validarRestauranteExistente;

    public List<Endereco> listarPorRestauranteId(Long restauranteId) {

        if (restauranteId != null) {
            validarRestauranteExistente.execute(restauranteId);
            return enderecoRepository.findByRestauranteId(restauranteId);

        } else {
            throw new BadRequestException("restaurante.nao.informado");
        }
    }
}
