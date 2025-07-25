package br.com.techchallenge.foodsys.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.ForbiddenException;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
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
