package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.endereco.AtualizarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.CriarEnderecoCommand;
import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoUsuarioComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoUsuarioCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoUsuarioComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.query.endereco.ListarEnderecoPorIdUsuario;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.utils.doc.EnderecoControllerDoc;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController implements EnderecoControllerDoc {

    private final CriarEnderecoCommand criarEnderecoCommand;
    private final AtualizarEnderecoComando atualizarEnderecoComando;
    private final DeletarEnderecoComando deletarEnderecoComando;
    private final ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;
    private final AutorizacaoService autorizacaoService;
    private final EnderecoRepository enderecoRepository;
    private final ValidadorPermissoes validadorPermissoes;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoUsuarioCommandDto dto) {
        validadorPermissoes.validarGerenciamentoDadosProprios(dto.getUsuarioId());

        criarEnderecoCommand.execute(dto.getUsuarioId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoUsuarioComandoDto dto) {
        Long usuarioId = dto.getUsuarioId();
        validadorPermissoes.validarGerenciamentoDadosProprios(usuarioId);

        atualizarEnderecoComando.execute(id, dto, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoUsuarioComandoDto dto) {
        Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
        validadorPermissoes.validarGerenciamentoDadosProprios(endereco.getUsuario().getId());

        deletarEnderecoComando.execute(dto.getUsuarioId(), dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long id) {
        validadorPermissoes.validarGerenciamentoDadosProprios(id);

        List<ListarEnderecoPorIdUsuarioResultadoItem> listarEnderecoPorIdUsuarioResultadoItem =
                listarEnderecoPorIdUsuario.execute(id);
        if(listarEnderecoPorIdUsuarioResultadoItem.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listarEnderecoPorIdUsuarioResultadoItem);
    }

}