package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecosQuery;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Endereços", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um endereço")

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

        private final CriarEnderecoCommand criarEnderecoCommand;
        private final AtualizarEnderecoComando atualizarEnderecoComando;
        private final DeletarEnderecoComando deletarEnderecoComando;
        private final AutorizacaoService autorizacaoService;
        private final EnderecoRepository enderecoRepository;
        private final ListarEnderecosQuery listarEnderecosQuery;

        @Operation(summary = "Criar um novo endereço.", description = "Recurso para criar um novo endereço.",

                        responses = {
                                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarEnderecoCommandDto.class))),

                                        @ApiResponse(responseCode = "409", description = "Endereço já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        @PostMapping
        public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoCommandDto dto) {
                autorizacaoService.validarAcessoUsuario(dto.getUsuarioId());

                criarEnderecoCommand.execute(dto);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar endereço do Usuário.", description = "Recurso para atualizar o endereço de um usuário.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarEnderecoComandoDto.class))),

                        @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<Void> atualizar(@PathVariable Long id,
                        @RequestBody @Valid AtualizarEnderecoComandoDto dto) {
                autorizacaoService.validarAcessoUsuario(dto.getUsuarioId());

                atualizarEnderecoComando.execute(id, dto);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping
        @Operation(summary = "Deletar um endereço.", description = "Recurso para deletar um endereço.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Recurso deleteado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletarEnderecoComandoDto.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não pode ser deletado por dadados de entradas inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })
        public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoComandoDto dto) {
                Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
                autorizacaoService.validarAcessoUsuario(endereco.getUsuario().getId());

                deletarEnderecoComando.execute(dto);
                return ResponseEntity.ok().build();
        }

        @GetMapping
        @Operation(summary = "Listar endereço por ID.", description = "Recurso para listar endereço do usuário por ID.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarEnderecoPorIdUsuario.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        public ResponseEntity<List<ListarEnderecoPorResultadoItem>> listarEnderecos(
                        @RequestParam(required = false) Long usuarioId,
                        @RequestParam(required = false) Long restauranteId) {

                ListarEnderecosParams params = new ListarEnderecosParams(usuarioId, restauranteId);
                List<ListarEnderecoPorResultadoItem> resultado = listarEnderecosQuery.execute(params);
                return ResponseEntity.ok(resultado);
        }
}