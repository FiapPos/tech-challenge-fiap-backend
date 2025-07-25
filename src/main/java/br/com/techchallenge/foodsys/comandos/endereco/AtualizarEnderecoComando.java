package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.AutorizacaoService;
import br.com.techchallenge.foodsys.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final CompartilhadoService sharedService;
    private final ValidarCepDoUsuario validarCepDoUsuario;
    private final AutorizacaoService autorizacaoService;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public Endereco execute(Long id, AtualizarEnderecoComandoDto dto, Long usuarioId) {
        validarDto(dto);
        Usuario usuarioLogado = autorizacaoService.getUsuarioLogado();
        validarProprietarioEndereco.execute(id, usuarioId);
        Endereco endereco = validarEnderecoExistente.execute(id);

        validarCepDoUsuario.validarCepDuplicado(usuarioLogado.getId(), dto.getCep());
        atualizarCampos(endereco, dto);
        return enderecoRepository.save(endereco);
    }

    private void validarDto(AtualizarEnderecoComandoDto dto) {
        if (!isPeloMenosUmCampoPreenchido(dto)) {
            throw new BadRequestException("atualizar.endereco.nenhum.campo");
        }
    }

    private boolean isPeloMenosUmCampoPreenchido(AtualizarEnderecoComandoDto dto) {
        return dto.getRua() != null || dto.getCep() != null ||
                dto.getNumero() != null;
    }

    private void atualizarCampos(Endereco endereco, AtualizarEnderecoComandoDto dto) {
        atualizarRua(endereco, dto.getRua());
        atualizarCep(endereco, dto.getCep());
        atualizarNumero(endereco, dto.getNumero());
        endereco.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    private void atualizarRua(Endereco endereco, String rua) {
        if (rua != null) {
            endereco.setRua(rua);
        }
    }

    private void atualizarCep(Endereco endereco, String cep) {
        if (cep != null) {
            endereco.setCep(cep);
        }
    }

    private void atualizarNumero(Endereco endereco, String numero) {
        if (numero != null) {
            endereco.setNumero(numero);
        }
    }

}