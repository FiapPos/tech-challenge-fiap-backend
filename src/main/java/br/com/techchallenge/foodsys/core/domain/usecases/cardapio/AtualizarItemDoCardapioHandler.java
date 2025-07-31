package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AtualizarItemDoCardapioHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public AtualizarItemDoCardapioHandler(ItemDoCardapioRepository itemDoCardapioRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public ItemDoCardapioResponseDTO executar(Long restauranteId, Long pratoId, AtualizarItemDoCardapioComando comando) {
        ItemDoCardapio itemDoCardapio = itemDoCardapioRepository
                .findByIdAndRestauranteId(pratoId, restauranteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado para o restaurante informado"));

        comando.aplicarEm(itemDoCardapio);

        ItemDoCardapio atualizado = itemDoCardapioRepository.save(itemDoCardapio);

        return ItemDoCardapioResponseDTO.fromEntity(atualizado);
    }
}
