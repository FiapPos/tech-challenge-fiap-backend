package br.com.techchallenge.foodsys.query.enderecoRestaurante;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarListaDeEnderecoRestaurante;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarEnderecoRestauranteQuery {

    private final ValidarListaDeEnderecoRestaurante validarListaDeEnderecoRestaurante;

    public List<ListarEnderecoPorRestauranteResultadoItem> execute(ListarEnderecosParams params) {
        List<Endereco> enderecos;

        Long restauranteId = params.getRestauranteId();

        enderecos = validarListaDeEnderecoRestaurante.listarPorRestauranteId(restauranteId);
        return mapToResultadoItemList(enderecos);
    }

    private List<ListarEnderecoPorRestauranteResultadoItem> mapToResultadoItemList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarEnderecoPorRestauranteResultadoItem mapToResultadoItem(Endereco endereco) {
        return ListarEnderecoPorRestauranteResultadoItem.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .dataCriacao(endereco.getDataCriacao())
                .dataAtualizacao(endereco.getDataAtualizacao())
                .build();
    }
}