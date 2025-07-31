package br.com.techchallenge.foodsys.core.utils.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Fotos dos Itens do Cardápio",
     description = "Contém todas as operações relativas ao gerenciamento de fotos dos itens do cardápio")
public interface FotoItemDoCardapioControllerDoc {

    @Operation(summary = "Upload de foto para item do cardápio",
               description = "Recurso para fazer upload de uma foto para um item específico do cardápio de um restaurante",
               responses = {
                   @ApiResponse(responseCode = "201", description = "Foto salva com sucesso",
                               content = @Content(mediaType = "text/plain",
                                                 schema = @Schema(implementation = String.class,
                                                                 example = "Foto salva com sucesso!"))),
                   @ApiResponse(responseCode = "400", description = "Arquivo inválido ou dados incorretos",
                               content = @Content(mediaType = "text/plain",
                                                 schema = @Schema(implementation = String.class,
                                                                 example = "Arquivo inválido"))),
                   @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para gerenciar este cardápio",
                               content = @Content(mediaType = "text/plain",
                                                 schema = @Schema(implementation = String.class,
                                                                 example = "Acesso negado"))),
                   @ApiResponse(responseCode = "404", description = "Restaurante ou item do cardápio não encontrado",
                               content = @Content(mediaType = "text/plain",
                                                 schema = @Schema(implementation = String.class,
                                                                 example = "Item não encontrado"))),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao processar o arquivo",
                               content = @Content(mediaType = "text/plain",
                                                 schema = @Schema(implementation = String.class,
                                                                 example = "Erro ao salvar a foto.")))
               })
    ResponseEntity<String> uploadFotoPrato(@PathVariable Long restauranteId,
                                          @PathVariable Long itemId,
                                          @RequestParam("arquivo") MultipartFile arquivo);
}
