package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarAcessoAdmin {

    private final AutorizacaoService autorizacaoService;

    public void validarPermissaoAdmin() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean isAdmin = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.ADMIN);

        if (!isAdmin) {
            throw new ForbiddenException("acesso.restrito.admin");
        }
    }

    public void validarOperacaoCompleta() {
        validarPermissaoAdmin();
    }
}

