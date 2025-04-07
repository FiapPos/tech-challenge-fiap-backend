package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CriarUsuarioCommand criarUsuarioCommand;

    @PostMapping
    public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioCommandDto criarUsuarioCommandDto) {
        criarUsuarioCommand.execute(criarUsuarioCommandDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}