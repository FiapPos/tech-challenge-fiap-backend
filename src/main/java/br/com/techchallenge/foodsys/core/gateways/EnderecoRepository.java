package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository {

    Endereco save(Endereco endereco);

    Optional<Endereco> findById(Long id);

    List<Endereco> findAll();

    void deleteById(Long id);

    List<Endereco> findByUsuarioId(Long usuarioId, Sort sort);

    List<Endereco> findByRestauranteId(Long restauranteId, Sort sort);

    boolean existsByUsuarioIdAndCep(Long usuarioId, String cep);

    boolean existsByRestauranteIdAndCep(Long restauranteId, String cep);

    boolean existsByUsuarioIdAndRestauranteId(Long usuarioId, Long restauranteId);

    boolean existsById(Long id);

    void deleteAll();
}