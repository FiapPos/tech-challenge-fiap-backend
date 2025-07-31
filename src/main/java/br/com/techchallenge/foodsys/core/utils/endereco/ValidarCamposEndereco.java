package br.com.techchallenge.foodsys.core.utils.endereco;

import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import org.springframework.stereotype.Component;


@Component
public class ValidarCamposEndereco {
    public void validar(String rua, String cep, String numero) {
        if (rua == null && cep == null && numero == null) {
            throw new BadRequestException("atualizar.endereco.nenhum.campo");
        }
    }
}
