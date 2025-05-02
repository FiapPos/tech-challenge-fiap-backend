package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
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
    public ResponseEntity<Endereco> criar(@RequestBody CriarEnderecoCommandDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEnderecoCommand.execute(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody AtualizarEnderecoComandoDto dto) {
        return ResponseEntity.ok(atualizarEnderecoComando.execute(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<Endereco> deletar(@RequestBody DeletarEnderecoComandoDto dto) {
        return ResponseEntity.ok(deletarEnderecoComando.execute(dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarEnderecoPorIdUsuario.execute(usuarioId));
    }
}