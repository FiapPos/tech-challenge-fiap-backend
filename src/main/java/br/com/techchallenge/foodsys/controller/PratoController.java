package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.cardapio.*;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoRequestDTO;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurante/{restauranteId}/pratos")
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
    public ResponseEntity<PratoResponseDTO> criar(
            @PathVariable Long restauranteId,
            @RequestBody @Valid PratoRequestDTO request) {


        CriarPratoComando comando = new CriarPratoComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal(),
                restauranteId
        );

        PratoResponseDTO resposta = criarPratoHandler.executar(comando);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<PratoResponseDTO>> listarTodos(@PathVariable Long restauranteId) {
        List<PratoResponseDTO> pratos = listarPratosHandler.executarPorRestaurante(restauranteId);
        return ResponseEntity.ok(pratos);
    }

    @GetMapping("/{pratoId}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(
            @PathVariable Long restauranteId,
            @PathVariable Long pratoId) {

        PratoResponseDTO prato = buscarPratoPorIdHandler.executar(restauranteId, pratoId);
        return ResponseEntity.ok(prato);
    }

    @PutMapping("/{pratoId}")
    public ResponseEntity<PratoResponseDTO> atualizar(
            @PathVariable Long restauranteId,
            @PathVariable Long pratoId,
            @RequestBody @Valid PratoRequestDTO request) {

        AtualizarPratoComando comando = new AtualizarPratoComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal()
        );

        PratoResponseDTO atualizado = atualizarPratoHandler.executar(restauranteId, pratoId, comando);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{pratoId}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long restauranteId,
            @PathVariable Long pratoId) {

        excluirPratoHandler.executar(restauranteId, pratoId);
        return ResponseEntity.noContent().build();
    }
}
