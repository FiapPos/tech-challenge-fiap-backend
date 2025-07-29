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
import br.com.techchallenge.foodsys.utils.doc.EnderecoRestauranteControllerDoc;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
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

        private final CriarEnderecoCommand criarEnderecoCommand;
        private final AtualizarEnderecoComando atualizarEnderecoComando;
        private final DeletarEnderecoComando deletarEnderecoComando;
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