package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.AtualizarRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.CriarRestauranteCommand;
import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.DesativarRestauranteComando;
import br.com.techchallenge.foodsys.core.dtos.restaurante.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.restaurante.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.core.queries.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.core.queries.restaurante.ListarRestaurantesQuery;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import br.com.techchallenge.foodsys.core.utils.doc.RestauranteControllerDoc;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurante")
@RequiredArgsConstructor
public class RestauranteController implements RestauranteControllerDoc {

        private final CriarRestauranteCommand criarRestauranteCommand;
        private final DesativarRestauranteComando desativarRestauranteComando;
        private final AtualizarRestauranteComando atualizarRestauranteComando;
        private final AutorizacaoService autorizacaoService;
        private final ListarRestaurantesQuery listarRestaurantesQuery;
        private final ValidadorPermissoes validadorPermissoes;

        @PostMapping
        public ResponseEntity<Void> criar(@RequestBody @Valid CriarRestauranteCommandDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante();
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                criarRestauranteCommand.execute(dto, usuario);
                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @GetMapping
        public ResponseEntity<List<ListarRestaurantesResultadoItem>> listarRestaurantes(
                        @ModelAttribute ListarRestaurantesParams params) {
                validadorPermissoes.validarVisualizacao();
                List<ListarRestaurantesResultadoItem> resultado = listarRestaurantesQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> desativarRestaurante(@PathVariable Long id) {
                validadorPermissoes.validarGerenciamentoRestaurante(id);
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                desativarRestauranteComando.execute(id);
                return ResponseEntity.ok().build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> atualizar(@PathVariable Long id,
                        @RequestBody @Valid AtualizarRestauranteComandoDto dto) {
                validadorPermissoes.validarGerenciamentoRestaurante(id);
                Usuario usuario = autorizacaoService.getUsuarioLogado();
                atualizarRestauranteComando.execute(id, dto, usuario);
                return ResponseEntity.ok().build();
        }
}
