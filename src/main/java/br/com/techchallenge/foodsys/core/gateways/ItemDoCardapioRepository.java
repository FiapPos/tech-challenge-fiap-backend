package br.com.techchallenge.foodsys.core.gateways;
import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import java.util.List;
import java.util.Optional;

public interface ItemDoCardapioRepository {
    ItemDoCardapio save(ItemDoCardapio itemDoCardapio);

    Optional<ItemDoCardapio> findById(Long id);

    List<ItemDoCardapio> findAll();

    void deleteById(Long id);

    List<ItemDoCardapio> findByRestauranteId(Long restauranteId);

    Optional<ItemDoCardapio> findByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByNomeAndRestauranteId(String nome, Long restauranteId);

    boolean existsById(Long id);

    void deleteAll();
}
