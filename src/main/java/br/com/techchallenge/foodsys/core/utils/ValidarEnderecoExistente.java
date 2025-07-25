package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarEnderecoExistente {

    private final EnderecoRepository enderecoRepository;

    public Endereco execute(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
    }

    public boolean validarEnderecoRestauranteExistente(Long restauranteId, Long usuarioId) {
        if (restauranteId == null) {
            return false;
        }

        boolean enderecoRestauranteExistente = enderecoRepository.existsByUsuarioIdAndRestauranteId(usuarioId,
                restauranteId);
        if (enderecoRestauranteExistente) {
            throw new BadRequestException("endereco.restaurante.ja.cadastrado");
        }
        return false;
    }
}