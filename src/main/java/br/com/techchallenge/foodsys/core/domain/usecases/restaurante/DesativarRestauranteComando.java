package br.com.techchallenge.foodsys.core.domain.usecases.restaurante;

import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DesativarRestauranteComando {

    private final RestauranteRepository restauranteRepository;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final CompartilhadoService sharedService;

    public Restaurante execute(Long id) {
        Restaurante restaurante = validarRestauranteExistente.execute(id);

        if (!restaurante.isAtivo()) {
            throw new BadRequestException("restaurante.ja.esta.desativado");
        }
        desativarRestaurante(restaurante);
        return restauranteRepository.save(restaurante);
    }

    private void desativarRestaurante(Restaurante restaurante) {
        restaurante.setAtivo(false);
        restaurante.setDataDesativacao(sharedService.getCurrentDateTime());
    }

}
