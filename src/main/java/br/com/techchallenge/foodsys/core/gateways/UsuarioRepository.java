package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    List<Usuario> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    List<Usuario> findByAtivo(boolean ativo);

    Optional<Usuario> findByLogin(String login);

    void deleteAll();
}