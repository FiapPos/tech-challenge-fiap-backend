package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ValidarDadosDelecaoEnderecoRestaurante {

    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public Endereco validarDelecao(Long enderecoId, Long usuarioId, Long restauranteId) {
        validarUsuarioExistente.execute(usuarioId);

        Endereco endereco = validarEnderecoExistente.execute(enderecoId);

        validarProprietarioEndereco.validarProprietarioEndereco(endereco, usuarioId, restauranteId);

        return endereco;
    }

}
