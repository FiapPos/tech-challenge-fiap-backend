package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {

    Restaurante save(Restaurante restaurante);

    Optional<Restaurante> findById(Long id);

    List<Restaurante> findAll();

    void deleteById(Long id);

    Restaurante findRestauranteByNome(String nome);

    Restaurante findByUsuarioId(Long usuarioDonoId);

    List<Restaurante> findByAtivo(boolean ativo);

    List<Restaurante> findByTipoCozinha(String tipoCozinha);

    List<Restaurante> findByAtivoAndTipoCozinha(Boolean ativo, String tipoCozinha);

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsById(Long id);

    void deleteAll();
}