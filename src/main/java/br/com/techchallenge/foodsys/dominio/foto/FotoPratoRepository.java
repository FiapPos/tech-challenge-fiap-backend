package br.com.techchallenge.foodsys.dominio.foto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FotoPratoRepository extends MongoRepository<FotoPratoDocumento, String> {
    Optional<FotoPratoDocumento> findByRestauranteIdAndPratoId(Long restauranteId, Long pratoId);
}
