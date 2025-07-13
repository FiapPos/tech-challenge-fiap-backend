package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidarProprietarioEndereco {
    public void validarProprietarioEndereco(Endereco endereco, Long usuarioId, Long restauranteId) {

        if (restauranteId != null) {

            if (!endereco.getRestaurante().getId().equals(restauranteId)) {
                throw new BadRequestException("endereco.nao.pertence.ao.restaurante");
            }
        } else {
            if (!endereco.getUsuario().getId().equals(usuarioId)) {
                throw new BadRequestException("endereco.nao.pertence.ao.usuario");
            }
        }
    }
}