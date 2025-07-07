package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AtualizarItemDoCardapioHandler {

    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public AtualizarItemDoCardapioHandler(ItemDoCardapioRepository itemDoCardapioRepository) {
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public ItemDoCardapioResponseDTO executar(Long restauranteId, Long pratoId, AtualizarItemDoCardapioComando comando) {
        ItemDoCardapio itemDoCardapio = (ItemDoCardapio) itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado para o restaurante informado"));

        itemDoCardapio.setNome(comando.getNome());
        itemDoCardapio.setDescricao(comando.getDescricao());
        itemDoCardapio.setPreco(comando.getPreco());
        itemDoCardapio.setDisponivelSomenteNoLocal(comando.getDisponivelSomenteNoLocal());

        ItemDoCardapio atualizado = itemDoCardapioRepository.save(itemDoCardapio);

        return ItemDoCardapioResponseDTO.fromEntity(atualizado);
    }
}
