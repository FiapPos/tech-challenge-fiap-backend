package br.com.techchallenge.foodsys.core.domain.usecases.endereco;

import br.com.techchallenge.foodsys.core.dtos.endereco.CriarEnderecoComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.shared.CompartilhadoService;
import br.com.techchallenge.foodsys.core.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarEnderecoCommand {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarCepDoUsuario validarCepDoUsuario;

    public Endereco execute(Long usuarioId, CriarEnderecoComandoDto criarEnderecoCommandDto) {
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarCepDoUsuario.validarCepDuplicado(usuario.getId(), criarEnderecoCommandDto.getCep());
        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoComandoDto dto, Usuario usuario) {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setUsuario(usuario);
        endereco.setDataCriacao(sharedService.getCurrentDateTime());
        return endereco;
    }
}