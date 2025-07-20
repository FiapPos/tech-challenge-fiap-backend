package br.com.techchallenge.foodsys.utils.usuario;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.ForbiddenException;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
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
