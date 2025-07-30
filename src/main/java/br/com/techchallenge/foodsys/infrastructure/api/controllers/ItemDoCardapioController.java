package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.usecases.cardapio.*;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioRequestDTO;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.utils.doc.ItemDoCardapioControllerDoc;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/itens")
public class ItemDoCardapioController implements ItemDoCardapioControllerDoc {

    private final CriarItemDoCardapioHandler criarItemDoCardapioHandler;
    private final ListarItemDoCardapioHandler listarItemDoCardapioHandler;
    private final BuscarItemDoCardapioPorIdHandler buscarItemDoCardapioPorIdHandler;
    private final AtualizarItemDoCardapioHandler atualizarItemDoCardapioHandler;
    private final ExcluirItemDoCardapioHandler excluirItemDoCardapioHandler;
    private final ValidadorPermissoes validadorPermissoes;

    public ItemDoCardapioController(
            CriarItemDoCardapioHandler criarItemDoCardapioHandler,
            ListarItemDoCardapioHandler listarItemDoCardapioHandler,
            BuscarItemDoCardapioPorIdHandler buscarItemDoCardapioPorIdHandler,
            AtualizarItemDoCardapioHandler atualizarItemDoCardapioHandler,
            ExcluirItemDoCardapioHandler excluirItemDoCardapioHandler,
            ValidadorPermissoes validadorPermissoes
    ) {
        this.criarItemDoCardapioHandler = criarItemDoCardapioHandler;
        this.listarItemDoCardapioHandler = listarItemDoCardapioHandler;
        this.buscarItemDoCardapioPorIdHandler = buscarItemDoCardapioPorIdHandler;
        this.atualizarItemDoCardapioHandler = atualizarItemDoCardapioHandler;
        this.excluirItemDoCardapioHandler = excluirItemDoCardapioHandler;
        this.validadorPermissoes = validadorPermissoes;
    }

    @PostMapping
    public ResponseEntity<ItemDoCardapioResponseDTO> criar(
            @PathVariable Long restauranteId,
            @RequestBody @Valid ItemDoCardapioRequestDTO request) {

        validadorPermissoes.validarGerenciamentoCardapio(restauranteId);

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal(),
                restauranteId
        );

        ItemDoCardapioResponseDTO resposta = criarItemDoCardapioHandler.executar(comando);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ItemDoCardapioResponseDTO>> listarTodos(@PathVariable Long restauranteId) {
        validadorPermissoes.validarVisualizacao();
        List<ItemDoCardapioResponseDTO> itens = listarItemDoCardapioHandler.executarPorRestaurante(restauranteId);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDoCardapioResponseDTO> buscarPorId(
            @PathVariable Long restauranteId,
            @PathVariable Long itemId) {

        validadorPermissoes.validarVisualizacao();
        ItemDoCardapioResponseDTO item = buscarItemDoCardapioPorIdHandler.executar(restauranteId, itemId);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDoCardapioResponseDTO> atualizar(
            @PathVariable Long restauranteId,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemDoCardapioRequestDTO request) {

        validadorPermissoes.validarGerenciamentoCardapio(restauranteId);

        AtualizarItemDoCardapioComando comando = new AtualizarItemDoCardapioComando(
                request.getNome(),
                request.getDescricao(),
                request.getPreco(),
                request.getDisponivelSomenteNoLocal()
        );

        ItemDoCardapioResponseDTO atualizado = atualizarItemDoCardapioHandler.executar(restauranteId, itemId, comando);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long restauranteId,
            @PathVariable Long itemId) {

        validadorPermissoes.validarGerenciamentoCardapio(restauranteId);
        excluirItemDoCardapioHandler.executar(restauranteId, itemId);
        return ResponseEntity.noContent().build();
    }
}
