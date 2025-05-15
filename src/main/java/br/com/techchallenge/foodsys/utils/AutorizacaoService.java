package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.excpetion.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenException("usuario.nao.autenticado");
        }

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new ForbiddenException("usuario.nao.encontrado"));
    }

    public void validarAcessoUsuario(Long usuarioId) {
        Usuario usuarioAutenticado = getUsuarioLogado();

        if (!usuarioAutenticado.getId().equals(usuarioId)) {
            throw new ForbiddenException("acesso.nao.autorizado");
        }
    }
}
