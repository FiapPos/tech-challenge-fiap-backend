package br.com.techchallenge.foodsys.dominio.cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PratoRepository extends JpaRepository<Prato, Long> {
    List<Prato> findByRestauranteId(Long restauranteId);

     Optional<Prato> findByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByIdAndRestauranteId(Long pratoId, Long restauranteId);
}
