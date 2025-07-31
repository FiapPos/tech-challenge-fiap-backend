package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.infrastructure.data.entities.RestauranteEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, Long> {

    RestauranteEntity findRestauranteByNome(String nome);

    RestauranteEntity findByUsuarioId(Long usuarioDonoId, Sort sort);

    List<RestauranteEntity> findByAtivo(boolean ativo, Sort sort);

    List<RestauranteEntity> findByTipoCozinha(String tipoCozinha);

    List<RestauranteEntity> findByAtivoAndTipoCozinha(Boolean ativo, String tipoCozinha);

    boolean existsByUsuarioId(Long usuarioId);
} 