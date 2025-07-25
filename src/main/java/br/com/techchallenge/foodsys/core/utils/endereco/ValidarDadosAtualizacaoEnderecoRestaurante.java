package br.com.techchallenge.foodsys.core.utils.endereco;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dto.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarCamposEndereco;
import br.com.techchallenge.foodsys.utils.ValidarCepRestauranteDuplicado;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarDadosAtualizacaoEnderecoRestaurante {

    private final ValidarCepRestauranteDuplicado validarCepRestauranteDuplicado;
    private final ValidarEnderecoExistente validarEnderecoExistente;

    private final ValidarCamposEndereco validarCamposEndereco;

    public Endereco validarAtualizacao(Long enderecoId, AtualizarEnderecoRestauranteComandoDto dto, Usuario usuario) {

        Endereco endereco = validarEnderecoExistente.execute(enderecoId);

        validarCepRestauranteDuplicado.validarCep(dto.getRestauranteId(), dto.getCep());

        return endereco;
    }
}
