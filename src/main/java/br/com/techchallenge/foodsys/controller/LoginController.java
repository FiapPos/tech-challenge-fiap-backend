package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class LoginController {

    private final AutenticaJwtComando autenticaJwtService;
    private final AutenticaLoginComando autenticaLoginService;

    public LoginController(AutenticaJwtComando autenticaJwtService, AutenticaLoginComando autenticaLoginService) {
        this.autenticaJwtService = autenticaJwtService;
        this.autenticaLoginService = autenticaLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid CredenciaisUsuarioDto credentials) {
        Usuario user = autenticaLoginService.login(credentials);
        String token = autenticaJwtService.createToken(user);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
