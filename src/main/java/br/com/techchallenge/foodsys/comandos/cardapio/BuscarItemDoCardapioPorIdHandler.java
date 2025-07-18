package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
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
