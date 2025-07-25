package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query("SELECT e FROM Endereco e JOIN FETCH e.usuario WHERE e.usuario.id = :usuarioId")
    List<Endereco> findByUsuarioId(@Param("usuarioId") Long usuarioId, Sort sort);

    @Query("SELECT e FROM Endereco e JOIN FETCH e.restaurante WHERE e.restaurante.id = :restauranteId")
    List<Endereco> findByRestauranteId(@Param("restauranteId") Long restauranteId, Sort sort);

    boolean existsByUsuarioIdAndCep(Long usuarioId, String cep);

    boolean existsByRestauranteIdAndCep(Long restauranteId, String cep);

    boolean existsByUsuarioIdAndRestauranteId(Long usuarioId, Long restauranteId);
}