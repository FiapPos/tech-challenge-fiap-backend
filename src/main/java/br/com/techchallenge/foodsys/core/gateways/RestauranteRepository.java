package br.com.techchallenge.foodsys.core.gateways;

import java.util.List;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Restaurante findRestauranteByNome(String nome);

    Restaurante findByUsuarioId(Long usuarioDonoId, Sort sort);

    List<Restaurante> findByAtivo(boolean ativo, Sort sort);

    List<Restaurante> findByTipoCozinha(String tipoCozinha);

    List<Restaurante> findByAtivoAndTipoCozinha(Boolean ativo, String tipoCozinha);

    boolean existsByUsuarioId(Long usuarioId);


}