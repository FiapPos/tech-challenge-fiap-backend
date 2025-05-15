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
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final CriarEnderecoCommand criarEnderecoCommand;
    private final AtualizarEnderecoComando atualizarEnderecoComando;
    private final DeletarEnderecoComando deletarEnderecoComando;
    private final ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;
    private final AutorizacaoService autorizacaoService;
    private final EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoCommandDto dto) {
        autorizacaoService.validarAcessoUsuario(dto.getUsuarioId());

        criarEnderecoCommand.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoComandoDto dto) {
        autorizacaoService.validarAcessoUsuario(dto.getUsuarioId());
        validarEnderecoPertenceAoUsuario(id, dto.getUsuarioId());

        atualizarEnderecoComando.execute(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoComandoDto dto) {
        Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
        autorizacaoService.validarAcessoUsuario(endereco.getUsuario().getId());

        deletarEnderecoComando.execute(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long id) {
        autorizacaoService.validarAcessoUsuario(id);

        List<ListarEnderecoPorIdUsuarioResultadoItem> listarEnderecoPorIdUsuarioResultadoItem =
                listarEnderecoPorIdUsuario.execute(id);
        if(listarEnderecoPorIdUsuarioResultadoItem.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listarEnderecoPorIdUsuarioResultadoItem);
    }

    private void validarEnderecoPertenceAoUsuario(Long enderecoId, Long usuarioId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));

        if (!endereco.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("endereco.nao.pertence.ao.usuario");
        }
    }
}