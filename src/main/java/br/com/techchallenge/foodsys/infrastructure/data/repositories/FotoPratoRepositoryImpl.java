package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.FotoPratoDocumento;
import br.com.techchallenge.foodsys.core.gateways.FotoPratoRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.FotoPratoDocumentoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FotoPratoRepositoryImpl implements FotoPratoRepository {

    private final FotoPratoMongoRepository mongoRepository;

    public FotoPratoRepositoryImpl(FotoPratoMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public FotoPratoDocumento save(FotoPratoDocumento fotoPratoDocumento) {
        FotoPratoDocumentoEntity entity = FotoPratoDocumentoEntity.fromDomain(fotoPratoDocumento);
        FotoPratoDocumentoEntity savedEntity = mongoRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<FotoPratoDocumento> findById(String id) {
        return mongoRepository.findById(id)
                .map(FotoPratoDocumentoEntity::toDomain);
    }

    @Override
    public List<FotoPratoDocumento> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(FotoPratoDocumentoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public Optional<FotoPratoDocumento> findByRestauranteIdAndPratoId(Long restauranteId, Long pratoId) {
        return mongoRepository.findByRestauranteIdAndPratoId(restauranteId, pratoId)
                .map(FotoPratoDocumentoEntity::toDomain);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        mongoRepository.deleteAll();
    }
} 