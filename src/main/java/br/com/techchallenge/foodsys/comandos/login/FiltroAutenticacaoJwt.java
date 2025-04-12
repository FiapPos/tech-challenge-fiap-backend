package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroAutenticacaoJwt extends OncePerRequestFilter {

    private final AutenticaJwtComando jwtService;
    private final AutenticaUsuarioComando userAuthenticationService;

    public FiltroAutenticacaoJwt(AutenticaJwtComando jwtService, AutenticaUsuarioComando userAuthenticationService) {
        this.jwtService = jwtService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            tryToAuthenticate(authorizationHeader);
        }

        filterChain.doFilter(request, response);
    }

    private void tryToAuthenticate(String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        try {
            String login = jwtService.getLogin(token);
            Usuario user = userAuthenticationService.getByLogin(login);
            DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(user);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
