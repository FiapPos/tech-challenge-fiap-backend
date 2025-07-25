package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarAcessoCliente {

    private final AutorizacaoService autorizacaoService;

    public void validarPermissaoCliente() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean isCliente = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.CLIENTE);

        if (!isCliente) {
            throw new ForbiddenException("acesso.restrito.cliente");
        }
    }

    public void validarVisualizacao() {
        validarPermissaoCliente();
    }

    public void validarGerenciamentoPropriosEnderecos(Long usuarioId) {
        validarPermissaoCliente();
        autorizacaoService.validarAcessoUsuario(usuarioId);
    }

    public void validarAtualizacaoPropriosDados(Long usuarioId) {
        validarPermissaoCliente();
        autorizacaoService.validarAcessoUsuario(usuarioId);
    }
}
