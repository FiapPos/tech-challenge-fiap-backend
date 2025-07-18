package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
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