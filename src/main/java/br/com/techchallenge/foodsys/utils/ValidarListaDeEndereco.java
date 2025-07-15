package br.com.techchallenge.foodsys.utils;

import java.util.List;

import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

@Service
@RequiredArgsConstructor

public class ValidarListaDeEndereco {

    private final EnderecoRepository enderecoRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;

    public List<Endereco> listarEnderecos(Long usuarioId, Long restauranteId) {

        if (restauranteId != null) {
            validarRestauranteExistente.execute(restauranteId);
            return enderecoRepository.findByRestauranteId(restauranteId);

        } else if (usuarioId != null) {
            validarUsuarioExistente.execute(usuarioId);
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            return enderecoRepository.findByUsuarioId(usuarioId, sort);

        } else {
            throw new BadRequestException("usuario.id.ou.restaurante.id.obrigatorio");
        }

    }
}
