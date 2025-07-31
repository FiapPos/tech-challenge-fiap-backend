package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.ItemDoCardapioEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemDoCardapioRepositoryImpl implements ItemDoCardapioRepository {

    private final ItemDoCardapioJpaRepository jpaRepository;

    public ItemDoCardapioRepositoryImpl(ItemDoCardapioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ItemDoCardapio save(ItemDoCardapio itemDoCardapio) {
        ItemDoCardapioEntity entity = ItemDoCardapioEntity.fromDomain(itemDoCardapio);
        ItemDoCardapioEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ItemDoCardapio> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ItemDoCardapioEntity::toDomain);
    }

    @Override
    public List<ItemDoCardapio> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ItemDoCardapioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<ItemDoCardapio> findByRestauranteId(Long restauranteId) {
        return jpaRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(ItemDoCardapioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDoCardapio> findByIdAndRestauranteId(Long pratoId, Long restauranteId) {
        return jpaRepository.findByIdAndRestauranteId(pratoId, restauranteId)
                .map(ItemDoCardapioEntity::toDomain);
    }

    @Override
    public boolean existsByIdAndRestauranteId(Long pratoId, Long restauranteId) {
        return jpaRepository.existsByIdAndRestauranteId(pratoId, restauranteId);
    }

    @Override
    public boolean existsByNomeAndRestauranteId(String nome, Long restauranteId) {
        return jpaRepository.existsByNomeAndRestauranteId(nome, restauranteId);
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