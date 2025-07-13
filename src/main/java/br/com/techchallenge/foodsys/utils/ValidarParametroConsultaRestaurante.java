package br.com.techchallenge.foodsys.utils;

import java.util.List;

import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.query.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;

@Service
@RequiredArgsConstructor

public class ValidarParametroConsultaRestaurante {

    private final RestauranteRepository restauranteRepository;

    public List<Restaurante> validarParametrosConsultaRestaurante(ListarRestaurantesParams params) {

        boolean temParmAtivo = params.getAtivo() != null;
        boolean temParmTipoCozinha = params.getTipoCozinha() != null && !params.getTipoCozinha().isEmpty();

        if (temParmAtivo && temParmTipoCozinha) {

            return restauranteRepository.findByAtivoAndTipoCozinha(
                    params.getAtivo(), params.getTipoCozinha());
        } else if (temParmTipoCozinha) {

            return restauranteRepository.findByTipoCozinha(params.getTipoCozinha());
        } else if (temParmAtivo) {

            return restauranteRepository.findByAtivo(params.getAtivo(), Sort.by(Sort.Direction.ASC, "id"));
        } else {

            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            return restauranteRepository.findAll(sort);
        }
    }

}
