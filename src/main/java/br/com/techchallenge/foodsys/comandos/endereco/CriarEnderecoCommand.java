package br.com.techchallenge.foodsys.comandos.endereco;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosCriacaoEndereco;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarEnderecoCommand {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarDadosCriacaoEndereco validarDadosDeEndereco;

    public Endereco execute(CriarEnderecoCommandDto criarEnderecoCommandDto, Usuario usuario) {

        validarDadosDeEndereco.validarCriacao(criarEnderecoCommandDto, usuario);

        Restaurante restaurante = validarDadosDeEndereco.obterRestauranteSeNecessario(criarEnderecoCommandDto, usuario);

        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario, restaurante);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoCommandDto dto, Usuario usuario, Restaurante restaurante) {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setDataCriacao(sharedService.getCurrentDateTime());
        return endereco;
    }

}