package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.infrastructure.data.entities.UsuarioEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    
    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    List<UsuarioEntity> findByAtivo(boolean ativo, Sort sort);

    Optional<UsuarioEntity> findByLogin(String login);
} 