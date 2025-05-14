package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final CriarEnderecoCommand criarEnderecoCommand;
    private final AtualizarEnderecoComando atualizarEnderecoComando;
    private final DeletarEnderecoComando deletarEnderecoComando;
    private final ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoCommandDto dto) {
        criarEnderecoCommand.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoComandoDto dto) {
        atualizarEnderecoComando.execute(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoComandoDto dto) {
        deletarEnderecoComando.execute(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(listarEnderecoPorIdUsuario.execute(id));
    }
}