package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.infrastructure.data.entities.ItemDoCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDoCardapioJpaRepository extends JpaRepository<ItemDoCardapioEntity, Long> {
    
    List<ItemDoCardapioEntity> findByRestauranteId(Long restauranteId);

    Optional<ItemDoCardapioEntity> findByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByIdAndRestauranteId(Long pratoId, Long restauranteId);

    boolean existsByNomeAndRestauranteId(String nome, Long restauranteId);
} 