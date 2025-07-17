package br.com.techchallenge.foodsys.utils;

import org.springframework.stereotype.Component;

import br.com.techchallenge.foodsys.excpetion.BadRequestException;

@Component

public class ValidarCamposEndereco {
    public void validar(String rua, String cep, String numero) {
        if (rua == null && cep == null && numero == null) {
            throw new BadRequestException("atualizar.endereco.nenhum.campo");
        }
    }
}
