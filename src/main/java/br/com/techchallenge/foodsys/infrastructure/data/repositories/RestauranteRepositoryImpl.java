package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.RestauranteEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RestauranteRepositoryImpl implements RestauranteRepository {

    private final RestauranteJpaRepository jpaRepository;

    public RestauranteRepositoryImpl(RestauranteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        RestauranteEntity entity = RestauranteEntity.fromDomain(restaurante);
        RestauranteEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRepository.findById(id)
                .map(RestauranteEntity::toDomain);
    }

    @Override
    public List<Restaurante> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Restaurante findRestauranteByNome(String nome) {
        RestauranteEntity entity = jpaRepository.findRestauranteByNome(nome);
        return entity != null ? entity.toDomain() : null;
    }

    @Override
    public Restaurante findByUsuarioId(Long usuarioDonoId) {
        RestauranteEntity entity = jpaRepository.findByUsuarioId(usuarioDonoId, Sort.by(Sort.Direction.DESC, "id"));
        return entity != null ? entity.toDomain() : null;
    }

    @Override
    public List<Restaurante> findByAtivo(boolean ativo) {
        return jpaRepository.findByAtivo(ativo, Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> findByTipoCozinha(String tipoCozinha) {
        return jpaRepository.findByTipoCozinha(tipoCozinha)
                .stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> findByAtivoAndTipoCozinha(Boolean ativo, String tipoCozinha) {
        return jpaRepository.findByAtivoAndTipoCozinha(ativo, tipoCozinha)
                .stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsuarioId(Long usuarioId) {
        return jpaRepository.existsByUsuarioId(usuarioId);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
} 