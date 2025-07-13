package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.usuario.AtualizarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.comandos.usuario.DesativarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.ListarUsuariosQuery;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Usuários", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

        private final CriarUsuarioCommand criarUsuarioCommand;
        private final ListarUsuariosQuery listarUsuariosQuery;
        private final AtualizarUsuarioComando atualizarUsuarioComando;
        private final DesativarUsuarioComando desativarUsuarioComando;

        @Operation(summary = "Criar um novo usuário.", description = "Recurso para criar um novo usuário.",

                        responses = {
                                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarUsuarioCommandDto.class))),

                                        @ApiResponse(responseCode = "409", description = "Usuário já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não processador por dados de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        @PostMapping
        public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioCommandDto criarUsuarioCommandDto) {
                criarUsuarioCommand.execute(criarUsuarioCommandDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @Operation(summary = "Listar usuário por ID.", description = "Recurso para listar usuario ID.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarUsuariosResultadoItem.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        @GetMapping
        public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params) {
                List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @Operation(summary = "Atualizar o Usuário.", description = "Recurso para atualizar o usuário.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarUsuarioComandoDto.class))),

                        @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        @PutMapping("/{id}")
        public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id,
                        @RequestBody AtualizarUsuarioComandoDto dto) {
                atualizarUsuarioComando.execute(id, dto);
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Deletar um usuário.", description = "Recurso para deletar um usuário.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Recurso deleteado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DesativarUsuarioComando.class))),

                                        @ApiResponse(responseCode = "422", description = "Recurso não pode ser deletado por dadados de entradas inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
                desativarUsuarioComando.execute(id);
                return ResponseEntity.ok().build();
        }
}