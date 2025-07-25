package br.com.techchallenge.foodsys.core.utils.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.utils.*;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ValidarDadosCriacaoEnderecoRestaurante {

    private final ValidarCepRestauranteDuplicado validarCepDuplicado;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarProprietarioRestaurante validarProprietarioRestaurante;

    public Restaurante validarCriacao(CriarEnderecoRestauranteComandoDto dto, Usuario usuario) {

        validarUsuarioExistente.execute(usuario.getId());
        validarEnderecoExistente.validarEnderecoRestauranteExistente(dto.getRestauranteId(), usuario.getId());

        Restaurante restaurante = validarRestauranteExistente.execute(dto.getRestauranteId());

        validarProprietarioRestaurante.validarProprietario(restaurante, usuario.getId());

        validarCepDuplicado.validarCep(dto.getRestauranteId(), dto.getCep());

        return restaurante;
    }
}
