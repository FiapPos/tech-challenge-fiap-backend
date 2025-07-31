package br.com.techchallenge.foodsys.core.gateways;
import br.com.techchallenge.foodsys.core.domain.entities.FotoPratoDocumento;

import java.util.List;
import java.util.Optional;

public interface FotoPratoRepository {
    FotoPratoDocumento save(FotoPratoDocumento fotoPratoDocumento);

    Optional<FotoPratoDocumento> findById(String id);

    List<FotoPratoDocumento> findAll();

    void deleteById(String id);

    Optional<FotoPratoDocumento> findByRestauranteIdAndPratoId(Long restauranteId, Long pratoId);

    boolean existsById(String id);

    void deleteAll();
}
