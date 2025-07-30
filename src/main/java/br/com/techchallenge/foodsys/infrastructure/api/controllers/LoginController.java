package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.usecases.login.AtualizaCredenciaisComando;
import br.com.techchallenge.foodsys.core.domain.usecases.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.core.domain.usecases.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.core.dtos.login.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.core.dtos.login.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.utils.ValidaConfirmacaoDeSenha;
import br.com.techchallenge.foodsys.core.utils.doc.LoginControllerDoc;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController implements LoginControllerDoc {

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
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid CredenciaisUsuarioDto credentials, BindingResult bindingResult) throws Exception {

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
        String token = autenticaJwtComando.createToken(user, credentials.tipoUsuario());

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
