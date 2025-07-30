package br.com.techchallenge.foodsys.core.utils.doc;

import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioRequestDTO;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Itens do Cardápio",
     description = "Contém todas as operações relativas aos recursos para gerenciamento de itens do cardápio de restaurantes")
public interface ItemDoCardapioControllerDoc {

    @Operation(summary = "Criar um novo item do cardápio",
               description = "Recurso para criar um novo item no cardápio de um restaurante",
               responses = {
                   @ApiResponse(responseCode = "201", description = "Item criado com sucesso",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = ItemDoCardapioResponseDTO.class))),
                   @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class))),
                   @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para gerenciar este cardápio",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<ItemDoCardapioResponseDTO> criar(@PathVariable Long restauranteId,
                                                    @RequestBody @Valid ItemDoCardapioRequestDTO request);

    @Operation(summary = "Listar todos os itens do cardápio",
               description = "Recurso para listar todos os itens do cardápio de um restaurante",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = ItemDoCardapioResponseDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<List<ItemDoCardapioResponseDTO>> listarTodos(@PathVariable Long restauranteId);

    @Operation(summary = "Buscar item do cardápio por ID",
               description = "Recurso para buscar um item específico do cardápio por seu ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = ItemDoCardapioResponseDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Item não encontrado",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<ItemDoCardapioResponseDTO> buscarPorId(@PathVariable Long restauranteId,
                                                          @PathVariable Long itemId);

    @Operation(summary = "Atualizar item do cardápio",
               description = "Recurso para atualizar as informações de um item do cardápio",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = ItemDoCardapioResponseDTO.class))),
                   @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class))),
                   @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para gerenciar este cardápio",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class))),
                   @ApiResponse(responseCode = "404", description = "Item não encontrado",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<ItemDoCardapioResponseDTO> atualizar(@PathVariable Long restauranteId,
                                                        @PathVariable Long itemId,
                                                        @RequestBody @Valid ItemDoCardapioRequestDTO request);

    @Operation(summary = "Excluir item do cardápio",
               description = "Recurso para excluir um item do cardápio",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
                   @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para gerenciar este cardápio",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class))),
                   @ApiResponse(responseCode = "404", description = "Item não encontrado",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> excluir(@PathVariable Long restauranteId,
                                @PathVariable Long itemId);
}
