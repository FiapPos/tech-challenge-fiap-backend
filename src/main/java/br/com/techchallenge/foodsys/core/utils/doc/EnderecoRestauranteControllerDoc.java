package br.com.techchallenge.foodsys.core.utils.doc;

import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
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

@Tag(name = "Endereços", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um endereço")
public interface EnderecoRestauranteControllerDoc {

    @Operation(summary = "Criar um novo endereço para o Restaurante.",
               description = "Recurso para criar um novo endereço para o Restaurante.",
               responses = {
                       @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = CriarEnderecoRestauranteComandoDto.class))),
                       @ApiResponse(responseCode = "400", description = "Endereço já existente.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoRestauranteComandoDto dto);

    @Operation(summary = "Atualizar endereço do Restaurante.",
               description = "Recurso para atualizar o endereço do Restaurante.",
               responses = {
                       @ApiResponse(responseCode = "200", description = "Endereco atualizado com sucesso.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = AtualizarEnderecoRestauranteComandoDto.class))),
                       @ApiResponse(responseCode = "400", description = "Endereco nao encontrado.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoRestauranteComandoDto dto);

    @Operation(summary = "Deletar endereço do Restaurante.",
               description = "Recurso para deletar endereço do Restaurante.",
               responses = {
                       @ApiResponse(responseCode = "200", description = "Endereco deleteado com sucesso.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = DeletarEnderecoRestauranteComandoDto.class))),
                       @ApiResponse(responseCode = "400", description = "Endereco nao encontrado.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoRestauranteComandoDto dto);

    @Operation(summary = "Listar endereço do Restaurante por ID.",
               description = "Recurso para listar endereço.",
               responses = {
                       @ApiResponse(responseCode = "200", description = "Endereco listado com sucesso.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = ListarEnderecoPorRestauranteResultadoItem.class))),
                       @ApiResponse(responseCode = "400", description = "Endereco não encontrado.",
                                   content = @Content(mediaType = "application/json",
                                                     schema = @Schema(implementation = BadRequestException.class)))
               })
    ResponseEntity<List<ListarEnderecoPorRestauranteResultadoItem>> listarEnderecos(@PathVariable Long id);
}
