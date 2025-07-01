package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.cardapio.*;
import br.com.techchallenge.foodsys.comandos.cardapio.BuscarPratoPorIdHandler;
import br.com.techchallenge.foodsys.comandos.cardapio.ExcluirPratoHandler;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoRequestDTO;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    private final CriarPratoHandler criarPratoHandler;
    private final ListarPratosHandler listarPratosHandler;
    private final BuscarPratoPorIdHandler buscarPratoPorIdHandler;
    private final AtualizarPratoHandler atualizarPratoHandler;
    private final ExcluirPratoHandler excluirPratoHandler;

    public PratoController(
            CriarPratoHandler criarPratoHandler,
            ListarPratosHandler listarPratosHandler,
            BuscarPratoPorIdHandler buscarPratoPorIdHandler,
            AtualizarPratoHandler atualizarPratoHandler,
            ExcluirPratoHandler excluirPratoHandler
    ) {
        this.criarPratoHandler = criarPratoHandler;
        this.listarPratosHandler = listarPratosHandler;
        this.buscarPratoPorIdHandler = buscarPratoPorIdHandler;
        this.atualizarPratoHandler = atualizarPratoHandler;
        this.excluirPratoHandler = excluirPratoHandler;
    }

    @PostMapping
    public ResponseEntity<PratoResponseDTO> criar(@RequestBody @Valid PratoRequestDTO request) {
        CriarPratoComando comando = new CriarPratoComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal(),
                request.getCaminhoFoto(),
                request.getRestauranteId()
        );

        PratoResponseDTO resposta = criarPratoHandler.executar(comando);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public List<PratoResponseDTO> listarTodos() {
        return listarPratosHandler.executar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(@PathVariable Long id) {
        PratoResponseDTO prato = buscarPratoPorIdHandler.executar(id);
        return ResponseEntity.ok(prato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PratoRequestDTO request) {
        AtualizarPratoComando comando = new AtualizarPratoComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal(),
                request.getCaminhoFoto()
        );

        PratoResponseDTO atualizado = atualizarPratoHandler.executar(id, comando);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        excluirPratoHandler.executar(id);
        return ResponseEntity.noContent().build();
    }
}
