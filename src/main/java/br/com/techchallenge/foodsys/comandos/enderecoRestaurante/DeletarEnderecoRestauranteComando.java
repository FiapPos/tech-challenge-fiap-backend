package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosDelecaoEnderecoRestaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoRestauranteComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarDadosDelecaoEnderecoRestaurante validarDadosDelecao;

    public void execute(DeletarEnderecoRestauranteComandoDto dto, Usuario usuario) {

        Endereco endereco = validarDadosDelecao.validarDelecao(dto.getEnderecoId(), usuario.getId(),
                dto.getRestauranteId());
        deletarEndereco(endereco);
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}