package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.infrastructure.data.entities.UsuarioTipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioTipoJpaRepository extends JpaRepository<UsuarioTipoEntity, Long> {
} 