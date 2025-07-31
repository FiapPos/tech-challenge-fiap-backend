package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;

import java.util.List;
import java.util.Optional;

public interface UsuarioTipoRepository {
    UsuarioTipo save(UsuarioTipo usuarioTipo);

    Optional<UsuarioTipo> findById(Long id);

    List<UsuarioTipo> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();
}
