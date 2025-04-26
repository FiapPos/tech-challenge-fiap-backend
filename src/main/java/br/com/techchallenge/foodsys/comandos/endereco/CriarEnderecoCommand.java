package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarEnderecoCommand {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarUsuarioExistente validarUsuarioExistente;

    public Endereco execute(CriarEnderecoCommandDto criarEnderecoCommandDto) {
        Usuario usuario = validarUsuarioExistente.execute(criarEnderecoCommandDto.getUsuarioId());
        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoCommandDto dto, Usuario usuario) {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setAtivo(true);
        endereco.setUsuario(usuario);
        endereco.setDataCriacao(sharedService.getCurrentDateTime());
        return endereco;
    }
}