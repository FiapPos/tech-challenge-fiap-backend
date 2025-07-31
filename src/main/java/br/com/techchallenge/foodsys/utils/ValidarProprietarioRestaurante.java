package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidarProprietarioRestaurante {
    public void validarProprietario(Restaurante restaurante, Long usuarioDonoId) {
        if (!restaurante.getUsuario().getId().equals(usuarioDonoId)) {
            throw new BadRequestException("restaurante.nao.pertence.ao.usuario");
        }
    }
}