package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.usuario.AtualizarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.query.ListarUsuariosQuery;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CriarUsuarioCommand criarUsuarioCommand;
    private final ListarUsuariosQuery listarUsuariosQuery;
    private final AtualizarUsuarioComando atualizarUsuarioComando;

    @PostMapping
    public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioCommandDto criarUsuarioCommandDto) {
        criarUsuarioCommand.execute(criarUsuarioCommandDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params) {
        List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id,
                                                 @RequestBody AtualizarUsuarioComandoDto dto) {
        atualizarUsuarioComando.execute(id, dto);
        return ResponseEntity.ok().build();
    }
}