package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.enderecoRestaurante.ListarEnderecoPorIdRestaurante;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
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
@RequestMapping("/enderecoRestaurante")
@RequiredArgsConstructor
public class EnderecoRestauranteController {

        private final CriarEnderecoCommand criarEnderecoCommand;
        private final AtualizarEnderecoComando atualizarEnderecoComando;
        private final DeletarEnderecoComando deletarEnderecoComando;
        private final AutorizacaoService autorizacaoService;
        private final EnderecoRepository enderecoRepository;
        private final ListarEnderecoPorIdRestaurante listarEnderecosQuery;
        private final ValidadorPermissoes validadorPermissoes;

        @Operation(summary = "Criar um novo endereço para o Restaurante.", description = "Recurso para criar um novo endereço para o Restaurante.",

                        responses = {
                                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarEnderecoRestauranteComandoDto.class))),

                                        @ApiResponse(responseCode = "400", description = "Endereço já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        @PostMapping
        public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                criarEnderecoCommand.execute(usuario.getId(), dto);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar endereço do Restaurante.", description = "Recurso para atualizar o endereço do Restaurante.", responses = {
                        @ApiResponse(responseCode = "200", description = "Endereco atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarEnderecoRestauranteComandoDto.class))),

                        @ApiResponse(responseCode = "400", description = "Endereco nao encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })

        public ResponseEntity<Void> atualizar(@PathVariable Long id,
                        @RequestBody @Valid AtualizarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                atualizarEnderecoComando.execute(id, dto, usuario.getId());
                return ResponseEntity.ok().build();
        }

        @DeleteMapping
        @Operation(summary = "Deletar endereço do Restaurante.", description = "Recurso para deletar endereço do Restaurante.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Endereco deleteado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletarEnderecoRestauranteComandoDto.class))),

                                        @ApiResponse(responseCode = "400", description = "Endereco nao encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })
        public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                enderecoRepository.findById(dto.getEnderecoId())
                                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                deletarEnderecoComando.execute(usuario.getId(), dto);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/restaurante/{id}")
        @Operation(summary = "Listar endereço do Restaurante por ID.", description = "Recurso para listar endereço.",

                        responses = {
                                        @ApiResponse(responseCode = "200", description = "Endereco listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarEnderecoPorRestauranteResultadoItem.class))),

                                        @ApiResponse(responseCode = "400", description = "Endereco não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
                        })

        public ResponseEntity<List<ListarEnderecoPorRestauranteResultadoItem>> listarEnderecos(@PathVariable Long id) {
                validadorPermissoes.validarVisualizacao();
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                ListarEnderecosParams params = new ListarEnderecosParams(usuario.getId(), id);
                List<ListarEnderecoPorRestauranteResultadoItem> resultado = listarEnderecosQuery.execute(params);

                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }
}