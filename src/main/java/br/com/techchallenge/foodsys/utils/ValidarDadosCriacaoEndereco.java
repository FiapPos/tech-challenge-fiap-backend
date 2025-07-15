package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ValidarDadosCriacaoEndereco {

    private final ValidarCepDuplicado validarCepDuplicado;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarProprietarioRestaurante validarProprietarioRestaurante;

    public void validarCriacao(CriarEnderecoCommandDto dto, Usuario usuario) {
        validarUsuarioExistente.execute(usuario.getId());

        if (dto.getRestauranteId() != null) {
            validarEnderecoExistente.validarEnderecoRestauranteExistente(dto.getRestauranteId(), usuario.getId());
            Restaurante restaurante = validarRestauranteExistente.execute(dto.getRestauranteId());
            validarProprietarioRestaurante.validarProprietario(restaurante, usuario.getId());
        }

        validarCepDuplicado.validarCep(usuario.getId(), dto.getRestauranteId(), dto.getCep());
    }

    public Restaurante obterRestauranteSeNecessario(CriarEnderecoCommandDto dto, Usuario usuario) {
        if (dto.getRestauranteId() != null) {
            return validarRestauranteExistente.execute(dto.getRestauranteId());
        }
        return null;
    }
}
