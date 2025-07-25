package br.com.techchallenge.foodsys.core.gateways;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioTipoRepository extends JpaRepository<UsuarioTipo, Long> {
}
