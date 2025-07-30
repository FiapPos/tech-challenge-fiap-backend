package br.com.techchallenge.foodsys.core.utils.doc;

import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.DesativarRestauranteComando;
import br.com.techchallenge.foodsys.core.dtos.restaurante.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.restaurante.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.queries.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Restaurantes",
     description = "Contém todas as operações relativas ao cadastro, atualização, exclusão e consulta de um restaurante")
public interface RestauranteControllerDoc {

    @Operation(summary = "Criar um novo restaurante.",
               description = "Endpoint de criação um novo restaurante.",
               responses = {
                   @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = CriarRestauranteCommandDto.class))),
                   @ApiResponse(responseCode = "400", description = "Nome é obrigatório.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class))),
                   @ApiResponse(responseCode = "403", description = "Acesso não autorizado.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> criar(@RequestBody @Valid CriarRestauranteCommandDto dto);

    @Operation(summary = "Listar todos os restaurantes.",
               description = "Endpoint de consulta dos restaurantes.",
               parameters = {
                   @Parameter(name = "ativo", description = "Filtrar por restaurantes ativos (true/false).",
                             in = ParameterIn.QUERY, schema = @Schema(type = "boolean")),
                   @Parameter(name = "tipoCozinha", description = "Filtrar por tipo de cozinha.",
                             in = ParameterIn.QUERY, schema = @Schema(type = "string"))
               },
               responses = {
                   @ApiResponse(responseCode = "200", description = "Restaurantes listados com sucesso.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = ListarRestaurantesResultadoItem.class))),
                   @ApiResponse(responseCode = "400", description = "Nenhum restaurante encontrado.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<List<ListarRestaurantesResultadoItem>> listarRestaurantes(@ModelAttribute ListarRestaurantesParams params);

    @Operation(summary = "Desativar o restaurante.",
               description = "Desdativa um restaurante existente.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Restaurante desativado com sucesso.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = DesativarRestauranteComando.class))),
                   @ApiResponse(responseCode = "400", description = "Restaurante nao encontrado.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> desativarRestaurante(@PathVariable Long id);

    @Operation(summary = "Atualizar dados do Restaurante.",
               description = "Endpoint de atualização dos dados do restaurante.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = AtualizarRestauranteComandoDto.class))),
                   @ApiResponse(responseCode = "400", description = "Restaurante não encontrado.",
                               content = @Content(mediaType = "application/json",
                                                 schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarRestauranteComandoDto dto);
}
