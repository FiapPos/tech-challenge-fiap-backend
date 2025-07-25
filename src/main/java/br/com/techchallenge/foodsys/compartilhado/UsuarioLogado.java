package br.com.techchallenge.foodsys.compartilhado;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
public class UsuarioLogado {

    public Optional<Usuario> getUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return Optional.empty();
        Object principal = authentication.getPrincipal();
        if (principal instanceof DetalhesUsuarioDto(Usuario usuario)) {
            return Optional.ofNullable(usuario);
        }
        return Optional.empty();
    }

    public Long getUsuarioId() {
        return getUsuario().map(Usuario::getId).orElse(null);
    }
}