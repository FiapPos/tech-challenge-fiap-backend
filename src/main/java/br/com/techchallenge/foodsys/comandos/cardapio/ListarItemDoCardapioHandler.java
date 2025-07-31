package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarItemDoCardapioHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public ListarItemDoCardapioHandler(ItemDoCardapioRepository itemDoCardapioRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public List<ItemDoCardapioResponseDTO> executar() {
        return itemDoCardapioRepository.findAll()
                .stream()
                .map(ItemDoCardapioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ItemDoCardapioResponseDTO> executarPorRestaurante(Long restauranteId) {
        return itemDoCardapioRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(ItemDoCardapioResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
