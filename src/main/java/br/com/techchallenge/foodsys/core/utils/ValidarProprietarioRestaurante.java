package br.com.techchallenge.foodsys.core.utils;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidarProprietarioRestaurante {
    public void validarProprietario(Restaurante restaurante, Long usuarioDonoId) {
        if (!restaurante.getUsuario().getId().equals(usuarioDonoId)) {
            throw new BadRequestException("restaurante.nao.pertence.ao.usuario");
        }
    }
}