package br.com.techchallenge.foodsys.dominio.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioTipoRepository extends JpaRepository<UsuarioTipo, Long> {
}
