package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.usuario.AtualizarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.CriarUsuarioCommand;
import br.com.techchallenge.foodsys.comandos.usuario.DesativarUsuarioComando;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.query.ListarUsuariosQuery;
import br.com.techchallenge.foodsys.query.params.ListarUsuariosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.ListarUsuariosResultadoItem;
import br.com.techchallenge.foodsys.utils.doc.UsuarioControllerDoc;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
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