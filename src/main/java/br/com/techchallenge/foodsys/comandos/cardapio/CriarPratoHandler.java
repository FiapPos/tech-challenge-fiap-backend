package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarPratoHandler {

    private final PratoRepository pratoRepository;

    public CriarPratoHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public PratoResponseDTO executar(CriarPratoComando comando) {
        Prato prato = new Prato();
        prato.setNome(comando.getNome());
        prato.setDescricao(comando.getDescricao());
        prato.setPreco(comando.getPreco());
        prato.setDisponivelSomenteNoLocal(comando.isDisponivelSomenteNoLocal());
        prato.setCaminhoFoto(comando.getCaminhoFoto());

        Prato salvo = pratoRepository.save(prato);

        return PratoResponseDTO.fromEntity(salvo);
    }


}
