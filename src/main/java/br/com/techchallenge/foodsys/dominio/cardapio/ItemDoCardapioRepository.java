package br.com.techchallenge.foodsys.dominio.cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemDoCardapioRepository extends JpaRepository<ItemDoCardapio, Long> {
    List<ItemDoCardapio> findByRestauranteId(Long restauranteId);

     Optional<ItemDoCardapio> findByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByIdAndRestauranteId(Long pratoId, Long restauranteId);
}
