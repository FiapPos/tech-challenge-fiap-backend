package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.CredenciaisInvalidasException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AutenticaLoginComando {

    public final AuthenticationManager authenticationManager;

    public AutenticaLoginComando(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Usuario login(CredenciaisUsuarioDto credentials) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(credentials.login(),
                    credentials.senha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return ((DetalhesUsuarioDto) authentication.getPrincipal()).usuario();
        } catch (Exception e) {
            throw new CredenciaisInvalidasException();
        }
    }
}