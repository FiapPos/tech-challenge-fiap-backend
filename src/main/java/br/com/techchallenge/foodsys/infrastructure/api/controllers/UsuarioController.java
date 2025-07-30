package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.usecases.usuario.AtualizarUsuarioComando;
import br.com.techchallenge.foodsys.core.domain.usecases.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.core.domain.usecases.usuario.DesativarUsuarioComando;
import br.com.techchallenge.foodsys.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.queries.ListarUsuariosQuery;
import br.com.techchallenge.foodsys.core.queries.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
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

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerDoc {

        private final CriarUsuarioCommand criarUsuarioCommand;
        private final ListarUsuariosQuery listarUsuariosQuery;
        private final AtualizarUsuarioComando atualizarUsuarioComando;
        private final DesativarUsuarioComando desativarUsuarioComando;
        private final ValidadorPermissoes validadorPermissoes;

        @PostMapping
        public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioCommandDto criarUsuarioCommandDto) {
                criarUsuarioCommand.execute(criarUsuarioCommandDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @GetMapping
        public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params) {
                validadorPermissoes.validarListagemUsuarios();
                List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id,
                        @RequestBody AtualizarUsuarioComandoDto dto) {
                validadorPermissoes.validarGerenciamentoDadosProprios(id);
                atualizarUsuarioComando.execute(id, dto);
                return ResponseEntity.ok().build();
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
                validadorPermissoes.validarDesativacaoUsuario();
                desativarUsuarioComando.execute(id);
                return ResponseEntity.ok().build();
        }
}