package br.com.techchallenge.foodsys.infrastructure.services;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarProprietarioEndereco {

    private final EnderecoRepository enderecoRepository;

    public void execute(Long enderecoId, Long usuarioId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));

        if (!endereco.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("endereco.nao.pertence.ao.usuario");
        }
    }
} 