package br.com.techchallenge.foodsys.core.utils.usuario.tipo;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificarTipoUsuarioExistente {

    public void execute(Usuario usuario, TipoUsuario tipo) {
        boolean tipoJaExiste = verificarTipoExiste(usuario, tipo);

        if (tipoJaExiste) {
            throw new BadRequestException("tipo.usuario.ja.existente");
        }
    }

    private boolean verificarTipoExiste(Usuario usuario, TipoUsuario tipo) {
        return usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == tipo);
    }
}
