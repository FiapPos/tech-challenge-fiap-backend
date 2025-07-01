package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AtualizarPratoHandler {

    private final PratoRepository pratoRepository;

    public AtualizarPratoHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public PratoResponseDTO executar(Long id, AtualizarPratoComando comando) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));

        prato.setNome(comando.getNome());
        prato.setDescricao(comando.getDescricao());
        prato.setPreco(comando.getPreco());
        prato.setDisponivelSomenteNoLocal(comando.getDisponivelSomenteNoLocal());
        prato.setCaminhoFoto(comando.getCaminhoFoto());

        Prato atualizado = pratoRepository.save(prato);

        return PratoResponseDTO.fromEntity(atualizado);
    }
}
