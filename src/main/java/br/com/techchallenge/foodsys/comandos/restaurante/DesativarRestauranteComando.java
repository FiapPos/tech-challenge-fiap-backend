package br.com.techchallenge.foodsys.comandos.restaurante;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DesativarRestauranteComando {

    private final RestauranteRepository restauranteRepository;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final CompartilhadoService sharedService;

    public Restaurante execute(Long id) {
        Restaurante restaurante = validarRestauranteExistente.execute(id);
        desativarRestaurante(restaurante);
        return restauranteRepository.save(restaurante);
    }

    private void desativarRestaurante(Restaurante restaurante) {
        restaurante.setAtivo(false);
        restaurante.setDataDesativacao(sharedService.getCurrentDateTime());
    }

}
