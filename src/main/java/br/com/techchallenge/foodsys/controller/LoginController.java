package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.login.AtualizaCredenciaisComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidaConfirmacaoDeSenha;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final AutenticaJwtComando autenticaJwtComando;
    private final AutenticaLoginComando autenticaLoginComando;
    private final AtualizaCredenciaisComando atualizaCredenciaisComando;
    private final ValidaConfirmacaoDeSenha validaConfirmacaoDeSenha;

    public LoginController(AutenticaJwtComando autenticaJwtComando,
                           AutenticaLoginComando autenticaLoginComando,
                           AtualizaCredenciaisComando atualizaCredenciaisComando,
                           ValidaConfirmacaoDeSenha validaConfirmacaoDeSenha) {
        this.autenticaJwtComando = autenticaJwtComando;
        this.autenticaLoginComando = autenticaLoginComando;
        this.atualizaCredenciaisComando = atualizaCredenciaisComando;
        this.validaConfirmacaoDeSenha = validaConfirmacaoDeSenha;
    }

    @InitBinder("atualizaCredenciaisComandoDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validaConfirmacaoDeSenha);
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid CredenciaisUsuarioDto credentials, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> erros = new HashMap<>();
            bindingResult.
                    getFieldErrors()
                    .forEach(error ->
                            erros.put(error.getField(), error.getDefaultMessage())
                    );
            return ResponseEntity.badRequest().body(erros);
        }

        Usuario user = autenticaLoginComando.login(credentials);
        String token = autenticaJwtComando.createToken(user);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Transactional
    @PutMapping("/login/atualiza-senha")
    public ResponseEntity<Map<String, String>> atualizaSenha(@RequestBody @Valid AtualizaCredenciaisComandoDto atualizaCredenciaisComandoDto,
                                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> erros = new HashMap<>();
            bindingResult.
                    getFieldErrors()
                    .forEach(error ->
                        erros.put(error.getField(), error.getDefaultMessage())
                    );
            return ResponseEntity.badRequest().body(erros);
        }

        atualizaCredenciaisComando.execute(atualizaCredenciaisComandoDto);
        return ResponseEntity.ok().build();
    }
}
