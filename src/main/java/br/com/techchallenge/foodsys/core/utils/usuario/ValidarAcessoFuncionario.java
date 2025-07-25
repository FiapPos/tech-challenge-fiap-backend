package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarAcessoFuncionario {

    private final AutorizacaoService autorizacaoService;

    public void validarPermissaoFuncionario() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean isFuncionario = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.FUNCIONARIO);

        if (!isFuncionario) {
            throw new ForbiddenException("acesso.restrito.funcionario");
        }
    }

    public void validarGerenciamentoCardapio() {
        validarPermissaoFuncionario();
    }

    public void validarUploadFoto() {
        validarPermissaoFuncionario();
    }

    public void validarVisualizacaoRestaurante() {
        validarPermissaoFuncionario();
    }
}
