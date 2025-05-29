package br.com.techchallenge.foodsys.dominio.usuario;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    List<Usuario> findByAtivo(boolean ativo, Sort sort);

    Optional<Usuario> findByLogin(String login);
}
