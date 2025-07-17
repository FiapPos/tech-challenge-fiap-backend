package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosAtualizacaoEnderecoRestaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarEnderecoRestauranteComando {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarDadosAtualizacaoEnderecoRestaurante validarDadosAtualizacaoEndereco;

    public Endereco execute(Long id, AtualizarEnderecoRestauranteComandoDto dto, Usuario usuario) {

        Endereco endereco = validarDadosAtualizacaoEndereco.validarAtualizacao(id, dto, usuario);

        atualizarCampos(endereco, dto);
        return enderecoRepository.save(endereco);
    }

    private void atualizarCampos(Endereco endereco, AtualizarEnderecoRestauranteComandoDto dto) {
        atualizarRua(endereco, dto.getRua());
        atualizarCep(endereco, dto.getCep());
        atualizarNumero(endereco, dto.getNumero());
        endereco.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    private void atualizarRua(Endereco endereco, String rua) {
        if (rua != null) {
            endereco.setRua(rua);
        }
    }

    private void atualizarCep(Endereco endereco, String cep) {
        if (cep != null) {
            endereco.setCep(cep);
        }
    }

    private void atualizarNumero(Endereco endereco, String numero) {
        if (numero != null) {
            endereco.setNumero(numero);
        }
    }
}