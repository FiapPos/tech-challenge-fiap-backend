package br.com.techchallenge.foodsys.core.gateways;
import br.com.techchallenge.foodsys.core.domain.entities.FotoPratoDocumento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FotoPratoRepository extends MongoRepository<FotoPratoDocumento, String> {
    Optional<FotoPratoDocumento> findByRestauranteIdAndPratoId(Long restauranteId, Long pratoId);
}
