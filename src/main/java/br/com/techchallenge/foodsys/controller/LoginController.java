package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.login.AtualizaCredenciaisComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaJwtComando;
import br.com.techchallenge.foodsys.comandos.login.AutenticaLoginComando;
import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidaConfirmacaoDeSenha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Login",
        description = "Contém as operações de autenticação e atualização de credenciais do usuário."
)

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

    @Operation(summary = "Autenticar login do usuário.",
            description = "Gera um token JWT para o usuário autenticado com login e senha válidos.",

            responses = {
                    @ApiResponse(responseCode = "201" , description = "Autenticação realizada com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))),

                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou campos obrigatórios não preenchidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BadRequestException.class)))
            })

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

    @Operation(
            summary = "Atualizar login do Usuário.",
            description = "Recurso para atualizar o login de um usuário.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Recurso atualizado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AtualizaCredenciaisComandoDto.class))),

                    @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BadRequestException.class)))
            })

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
