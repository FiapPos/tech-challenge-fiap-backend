package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.ForbiddenException;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.utils.usuario.ValidarAcessoAdmin;
import br.com.techchallenge.foodsys.utils.usuario.ValidarAcessoCliente;
import br.com.techchallenge.foodsys.utils.usuario.ValidarAcessoDonoRestaurante;
import br.com.techchallenge.foodsys.utils.usuario.ValidarAcessoFuncionario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidadorPermissoes {

    private final ValidarAcessoAdmin validarAcessoAdmin;
    private final ValidarAcessoDonoRestaurante validarAcessoDonoRestaurante;
    private final ValidarAcessoFuncionario validarAcessoFuncionario;
    private final ValidarAcessoCliente validarAcessoCliente;
    private final AutorizacaoService autorizacaoService;
    private final RestauranteRepository restauranteRepository;

    public void validarCriacaoUsuario() {
        validarAcessoAdmin.validarOperacaoCompleta();
    }

    public void validarListagemUsuarios() {
        validarAcessoAdmin.validarOperacaoCompleta();
    }

    public void validarDesativacaoUsuario() {
        validarAcessoAdmin.validarOperacaoCompleta();
    }

    public void validarGerenciamentoRestaurante() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean temPermissao = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.ADMIN ||
                        ut.getTipo() == TipoUsuario.DONO_RESTAURANTE);

        if (!temPermissao) {
            throw new ForbiddenException("acesso.restrito.gerenciamento.restaurante");
        }
    }

    public void validarGerenciamentoRestaurante(Long restauranteId) {
        Usuario usuario = autorizacaoService.getUsuarioLogado();

        boolean isAdmin = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.ADMIN);

        if (isAdmin) {
            return;
        }

        boolean isDonoRestaurante = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.DONO_RESTAURANTE);

        if (isDonoRestaurante) {
            validarPropriedadeRestaurante(usuario.getId(), restauranteId);
            return;
        }

        throw new ForbiddenException("acesso.restrito.gerenciamento.restaurante");
    }

    public void validarGerenciamentoCardapio() {
        Usuario usuario = autorizacaoService.getUsuarioLogado();
        boolean temPermissao = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.ADMIN ||
                        ut.getTipo() == TipoUsuario.DONO_RESTAURANTE ||
                        ut.getTipo() == TipoUsuario.FUNCIONARIO);

        if (!temPermissao) {
            throw new ForbiddenException("acesso.restrito.gerenciamento.cardapio");
        }
    }

    public void validarGerenciamentoCardapio(Long restauranteId) {
        Usuario usuario = autorizacaoService.getUsuarioLogado();

        boolean isAdmin = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.ADMIN);

        if (isAdmin) {
            return;
        }

        boolean isDonoRestaurante = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.DONO_RESTAURANTE);

        if (isDonoRestaurante) {
            validarPropriedadeRestaurante(usuario.getId(), restauranteId);
            return;
        }

        boolean isFuncionario = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.FUNCIONARIO);

        if (isFuncionario) {
            return;
        }

        throw new ForbiddenException("acesso.restrito.gerenciamento.cardapio");
    }

    private void validarPropriedadeRestaurante(Long usuarioId, Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new BadRequestException("restaurante.nao.encontrado"));

        if (!restaurante.getUsuarioId().equals(usuarioId)) {
            throw new ForbiddenException("acesso.restrito.restaurante.nao.proprio");
        }
    }

    public void validarVisualizacao() {
        autorizacaoService.getUsuarioLogado();
    }

    public void validarGerenciamentoDadosProprios(Long usuarioId) {
        autorizacaoService.getUsuarioLogado();
        autorizacaoService.validarAcessoUsuario(usuarioId);
    }
}