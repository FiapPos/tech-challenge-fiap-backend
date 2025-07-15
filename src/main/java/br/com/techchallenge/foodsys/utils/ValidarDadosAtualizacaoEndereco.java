package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ValidarDadosAtualizacaoEndereco {

    private final ValidarCepDuplicado validarCepDuplicado;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public Endereco validarAtualizacao(Long enderecoId, AtualizarEnderecoComandoDto dto, Usuario usuario) {
        validarDtoAtualizacao(dto);

        Endereco endereco = validarEnderecoExistente.execute(enderecoId);

        validarProprietarioEndereco.validarProprietarioEndereco(endereco, usuario.getId(), dto.getRestauranteId());
        validarCepDuplicado.validarCep(usuario.getId(), dto.getRestauranteId(), dto.getCep());

        return endereco;
    }

    private void validarDtoAtualizacao(AtualizarEnderecoComandoDto dto) {
        if (!isPeloMenosUmCampoPreenchido(dto)) {
            throw new BadRequestException("atualizar.endereco.nenhum.campo");
        }
    }

    private boolean isPeloMenosUmCampoPreenchido(AtualizarEnderecoComandoDto dto) {
        return dto.getRua() != null || dto.getCep() != null ||
                dto.getNumero() != null;
    }

}
