package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.excpetion.CredenciaisInvalidasException;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AutenticaLoginComando {

    private final AuthenticationManager authenticationManager;

    public AutenticaLoginComando(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Usuario login(CredenciaisUsuarioDto credentials) throws Exception {
        try {
            Authentication authentication = autenticarCredenciais(credentials);
            Usuario usuario = extrairUsuario(authentication);
            validarTipoUsuario(usuario, credentials.tipoUsuario());
            return usuario;
        } catch (Exception e) {
            tratarExcecao(e);
            return null;
        }
    }

    private Authentication autenticarCredenciais(CredenciaisUsuarioDto credentials) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                credentials.login(), credentials.senha());
        return authenticationManager.authenticate(token);
    }

    private Usuario extrairUsuario(Authentication authentication) {
        DetalhesUsuarioDto detalhes = (DetalhesUsuarioDto) authentication.getPrincipal();
        return detalhes.usuario();
    }

    private void validarTipoUsuario(Usuario usuario, br.com.techchallenge.foodsys.enums.TipoUsuario tipoSolicitado) {
        boolean temTipo = usuario.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == tipoSolicitado);

        if (!temTipo) {
            throw new CredenciaisInvalidasException();
        }
    }

    private void tratarExcecao(Exception e) throws Exception {
        if (e instanceof BadRequestException) {
            throw e;
        }
        throw new CredenciaisInvalidasException();
    }
}