package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarAcessoDonoRestaurante {

    private final AutorizacaoService autorizacaoService;

    public void validarPermissaoDonoRestaurante() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean isDonoRestaurante = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.DONO_RESTAURANTE);

        if (!isDonoRestaurante) {
            throw new ForbiddenException("acesso.restrito.dono.restaurante");
        }
    }

    public void validarGerenciamentoRestaurante() {
        validarPermissaoDonoRestaurante();
    }

    public void validarGerenciamentoCardapio() {
        validarPermissaoDonoRestaurante();
    }

    public void validarUploadFoto() {
        validarPermissaoDonoRestaurante();
    }
}
