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

    private static final String AUTHENTICATION_HEADER = "Bearer ";

    private final AutenticaJwtComando jwtService;
    private final AutenticaUsuarioComando userAuthenticationService;

    public FiltroAutenticacaoJwt(AutenticaJwtComando jwtService, AutenticaUsuarioComando userAuthenticationService) {
        this.jwtService = jwtService;
        this.userAuthenticationService = userAuthenticationService;
    }

    private static final String[] ROTAS_PUBLICAS = {
            "/login",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui/",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/h2-console",
            "/h2-console/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Ignora autenticação para rotas públicas
        for (String rotaPublica : ROTAS_PUBLICAS) {
            if (path.startsWith(rotaPublica)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHENTICATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHENTICATION_HEADER.length());
            if (!token.isEmpty()) tryToAuthenticate(token);
        }

        filterChain.doFilter(request, response);
    }

    private void tryToAuthenticate(String token) {
        try {
            String login = jwtService.getLogin(token);
            Usuario user = userAuthenticationService.getByLogin(login);
            DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(user);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        } catch (Exception e) {
            //do nothing
        }
    }
}
