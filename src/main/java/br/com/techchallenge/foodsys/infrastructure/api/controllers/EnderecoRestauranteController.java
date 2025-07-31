package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.AtualizarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.CriarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.DeletarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.queries.enderecoRestaurante.ListarEnderecoPorIdRestaurante;
import br.com.techchallenge.foodsys.core.queries.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import br.com.techchallenge.foodsys.core.utils.doc.EnderecoRestauranteControllerDoc;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecoRestaurante")
@RequiredArgsConstructor
public class EnderecoRestauranteController implements EnderecoRestauranteControllerDoc {

        private final CriarEnderecoRestauranteComando criarEnderecoCommand;
        private final AtualizarEnderecoRestauranteComando atualizarEnderecoComando;
        private final DeletarEnderecoRestauranteComando deletarEnderecoComando;
        private final AutorizacaoService autorizacaoService;
        private final EnderecoRepository enderecoRepository;
        private final ListarEnderecoPorIdRestaurante listarEnderecosQuery;
        private final ValidadorPermissoes validadorPermissoes;

        @PostMapping
        public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                criarEnderecoCommand.execute(usuario.getId(), dto);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> atualizar(@PathVariable Long id,
                        @RequestBody @Valid AtualizarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                atualizarEnderecoComando.execute(id, dto, usuario.getId());
                return ResponseEntity.ok().build();
        }

        @DeleteMapping
        public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                enderecoRepository.findById(dto.getEnderecoId())
                                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
                Usuario usuario = autorizacaoService.getUsuarioLogado();

                deletarEnderecoComando.execute(usuario.getId(), dto);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/restaurante/{id}")

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