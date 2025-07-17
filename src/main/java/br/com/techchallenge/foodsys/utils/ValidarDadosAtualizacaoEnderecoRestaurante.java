package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ValidarDadosAtualizacaoEnderecoRestaurante {

    private final ValidarCepRestauranteDuplicado validarCepRestauranteDuplicado;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;
    private final ValidarCamposEndereco validarCamposEndereco;

    public Endereco validarAtualizacao(Long enderecoId, AtualizarEnderecoRestauranteComandoDto dto, Usuario usuario) {
        validarCamposEndereco.validar(dto.getRua(), dto.getCep(), dto.getNumero());

        Endereco endereco = validarEnderecoExistente.execute(enderecoId);

        validarProprietarioEndereco.validarProprietarioEndereco(endereco, usuario.getId(), dto.getRestauranteId());
        validarCepRestauranteDuplicado.validarCep(dto.getRestauranteId(), dto.getCep());

        return endereco;
    }
}
