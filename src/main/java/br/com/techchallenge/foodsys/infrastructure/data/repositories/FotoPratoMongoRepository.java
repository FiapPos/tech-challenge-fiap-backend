package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.infrastructure.data.entities.FotoPratoDocumentoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FotoPratoMongoRepository extends MongoRepository<FotoPratoDocumentoEntity, String> {
    Optional<FotoPratoDocumentoEntity> findByRestauranteIdAndPratoId(Long restauranteId, Long pratoId);
} 