package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.util.Date;

@Service
public class AutenticaJwtComando {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;

    public String createToken(Usuario user, TipoUsuario tipoUsuario) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim("tipoUsuario", tipoUsuario.name())
                .withClaim("userId", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String getLogin(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }
}