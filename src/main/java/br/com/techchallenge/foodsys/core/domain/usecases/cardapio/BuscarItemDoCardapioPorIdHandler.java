package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;

import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.excpetion.ItemDoCardapioNaoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class BuscarItemDoCardapioPorIdHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public BuscarItemDoCardapioPorIdHandler(ItemDoCardapioRepository itemDoCardapioRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public ItemDoCardapioResponseDTO executar(Long restauranteId, Long pratoId) {
        return itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId)
                .map(ItemDoCardapioResponseDTO::fromEntity)
                .orElseThrow(() -> new ItemDoCardapioNaoEncontradoException(pratoId));
    }
}
