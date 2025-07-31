package br.com.techchallenge.foodsys.core.dtos.login;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record DetalhesUsuarioDto(Usuario usuario) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getUsuarioTipos().stream()
                .map(usuarioTipo -> new SimpleGrantedAuthority(usuarioTipo.getTipo().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isAtivo();
    }

    @Override
    public Usuario usuario() {
        return usuario;
    }
}