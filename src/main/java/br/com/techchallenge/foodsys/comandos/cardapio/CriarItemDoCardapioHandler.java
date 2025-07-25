package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarItemDoCardapioHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;
    private final RestauranteRepository restauranteRepository;

    public CriarItemDoCardapioHandler(ItemDoCardapioRepository itemDoCardapioRepository, RestauranteRepository restauranteRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public ItemDoCardapioResponseDTO executar(CriarItemDoCardapioComando comando) {
        Restaurante restaurante = restauranteRepository.findById(comando.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante com ID " + comando.getRestauranteId() + " não encontrado."));

        boolean nomeJaExiste = itemDoCardapioRepository.existsByNomeAndRestauranteId(comando.getNome(), comando.getRestauranteId());
        if (nomeJaExiste) {
            throw new IllegalArgumentException("Já existe um item com esse nome no cardápio do restaurante.");
        }

        ItemDoCardapio itemDoCardapio = new ItemDoCardapio();
        itemDoCardapio.setNome(comando.getNome());
        itemDoCardapio.setDescricao(comando.getDescricao());
        itemDoCardapio.setPreco(comando.getPreco());
        itemDoCardapio.setDisponivelSomenteNoLocal(comando.getDisponivelSomenteNoLocal());
        itemDoCardapio.setRestaurante(restaurante);

        ItemDoCardapio salvo = itemDoCardapioRepository.save(itemDoCardapio);
        return ItemDoCardapioResponseDTO.fromEntity(salvo);
    }

}
