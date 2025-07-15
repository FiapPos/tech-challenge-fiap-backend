package br.com.techchallenge.foodsys.query.endereco;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarListaDeEndereco;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarEnderecosQuery {

    private final ValidarListaDeEndereco validarListaDeEndereco;

    public List<ListarEnderecoPorResultadoItem> execute(ListarEnderecosParams params) {
        List<Endereco> enderecos;

        Long usuarioId = params.getUsuarioId();
        Long restauranteId = params.getRestauranteId();

        enderecos = validarListaDeEndereco.listarEnderecos(usuarioId, restauranteId);
        return mapToResultadoItemList(enderecos);
    }

    private List<ListarEnderecoPorResultadoItem> mapToResultadoItemList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarEnderecoPorResultadoItem mapToResultadoItem(Endereco endereco) {
        return ListarEnderecoPorResultadoItem.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .dataCriacao(endereco.getDataCriacao())
                .dataAtualizacao(endereco.getDataAtualizacao())
                .build();
    }
}