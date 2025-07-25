package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import java.util.List;

import br.com.techchallenge.foodsys.core.dtos.restaurante.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.queries.restaurante.ListarRestaurantesQuery;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.AtualizarRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.CriarRestauranteCommand;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.DesativarRestauranteComando;
import br.com.techchallenge.foodsys.core.dtos.restaurante.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.query.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.query.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Restaurantes", description = "Contém todas as operações relativas ao cadastro, atualização, exclusão e consulta de um restaurante")

@RestController
@RequestMapping("/restaurante")
@RequiredArgsConstructor
public class RestauranteController {

        private final CriarRestauranteCommand criarRestauranteCommand;
        private final DesativarRestauranteComando desativarRestauranteComando;
        private final AtualizarRestauranteComando atualizarRestauranteComando;
        private final AutorizacaoService autorizacaoService;
        private final ListarRestaurantesQuery listarRestaurantesQuery;
        private final ValidadorPermissoes validadorPermissoes;

        @PostMapping
        @Operation(summary = "Criar um novo restaurante.", description = "Endpoint de criação um novo restaurante.", responses = {
                        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarRestauranteCommandDto.class))),
                        @ApiResponse(responseCode = "400", description = "Nome é obrigatório.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "403", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<Void> criar(@RequestBody @Valid CriarRestauranteCommandDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante(); // Criação não precisa de ID específico
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                criarRestauranteCommand.execute(dto, usuario);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @GetMapping
        @Operation(summary = "Listar todos os restaurantes.", description = "Endpoint de consulta dos restaurantes.", parameters = {
                        @Parameter(name = "ativo", description = "Filtrar por restaurantes ativos (true/false).", in = ParameterIn.QUERY, schema = @Schema(type = "boolean")),
                        @Parameter(name = "tipoCozinha", description = "Filtrar por tipo de cozinha.", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
        }, responses = {
                        @ApiResponse(responseCode = "200", description = "Restaurantes listados com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarRestaurantesResultadoItem.class))),
                        @ApiResponse(responseCode = "400", description = "Nenhum restaurante encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<List<ListarRestaurantesResultadoItem>> listarRestaurantes(
                        @ModelAttribute ListarRestaurantesParams params) {
                validadorPermissoes.validarVisualizacao(); // Listagem geral não precisa validação específica
                List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Desativar o restaurante.", description = "Desdativa um restaurante existente.", responses = {
                        @ApiResponse(responseCode = "200", description = "Restaurante desativado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DesativarRestauranteComando.class))),
                        @ApiResponse(responseCode = "400", description = "Restaurante nao encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<Void> desativarRestaurante(@PathVariable Long id) {
                validadorPermissoes.validarGerenciamentoRestaurante(id); // Validar acesso ao restaurante específico
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                desativarRestauranteComando.execute(id);
                return ResponseEntity.ok().build();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar dados do Restaurante.", description = "Endpoint de atualização dos dados do restaurante.", responses = {
                        @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarRestauranteComandoDto.class))),

                        @ApiResponse(responseCode = "400", description = "Restaurante não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<Void> atualizar(@PathVariable Long id,
                        @RequestBody @Valid AtualizarRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante(id); // Validar acesso ao restaurante específico
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                atualizarRestauranteComando.execute(id, dto, usuario);
                return ResponseEntity.ok().build();
        }
}
