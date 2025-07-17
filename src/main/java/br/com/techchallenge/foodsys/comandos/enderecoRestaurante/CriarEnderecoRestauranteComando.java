package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosCriacaoEnderecoRestaurante;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarEnderecoRestauranteComando {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarDadosCriacaoEnderecoRestaurante validarDadosDeEndereco;

    public Endereco execute(CriarEnderecoRestauranteComandoDto criarEnderecoCommandDto, Usuario usuario) {

        Restaurante restaurante = validarDadosDeEndereco.validarCriacao(criarEnderecoCommandDto, usuario);

        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario, restaurante);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoRestauranteComandoDto dto, Usuario usuario, Restaurante restaurante) {
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