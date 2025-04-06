package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.command.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.command.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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