package br.com.techchallenge.foodsys.domain.usuario;

import br.com.techchallenge.foodsys.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
