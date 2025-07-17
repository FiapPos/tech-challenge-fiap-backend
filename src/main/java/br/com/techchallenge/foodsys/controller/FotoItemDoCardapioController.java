package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.cardapio.FotoItemDoCardapioHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/itens/{itemId}/foto")
public class FotoItemDoCardapioController {

    private final FotoItemDoCardapioHandler fotoItemDoCardapioHandler;

    public FotoItemDoCardapioController(FotoItemDoCardapioHandler fotoItemDoCardapioHandler) {
        this.fotoItemDoCardapioHandler = fotoItemDoCardapioHandler;
    }

    @PostMapping
    public ResponseEntity<String> uploadFotoPrato(
            @PathVariable Long restauranteId,
            @PathVariable Long pratoId,
            @RequestParam("arquivo") MultipartFile arquivo) {

        try {
            fotoItemDoCardapioHandler.salvarFoto(restauranteId, pratoId, arquivo);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Foto salva com sucesso!");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a foto.");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
