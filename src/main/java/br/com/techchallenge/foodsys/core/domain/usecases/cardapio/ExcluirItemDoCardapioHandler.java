package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;

import br.com.techchallenge.foodsys.core.exceptions.ItemDoCardapioNaoEncontradoException;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import org.springframework.stereotype.Service;

@Service
public class ExcluirItemDoCardapioHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public ExcluirItemDoCardapioHandler(ItemDoCardapioRepository itemDoCardapioRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public void executar(Long restauranteId, Long pratoId) {
        boolean exists = itemDoCardapioRepository.existsByIdAndRestauranteId(pratoId, restauranteId);
        if (exists) {
            itemDoCardapioRepository.deleteById(pratoId);
        } else {
            throw new ItemDoCardapioNaoEncontradoException(pratoId);
        }
    }
}
