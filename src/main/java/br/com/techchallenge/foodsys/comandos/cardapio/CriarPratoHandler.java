package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarPratoHandler {

    private final PratoRepository pratoRepository;
    private final RestauranteRepository restauranteRepository;

    public CriarPratoHandler(PratoRepository pratoRepository, RestauranteRepository restauranteRepository) {
        this.pratoRepository = pratoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public PratoResponseDTO executar(CriarPratoComando comando) {
        Restaurante restaurante = restauranteRepository.findById(comando.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante com ID " + comando.getRestauranteId() + " não encontrado."));

        Prato prato = new Prato();
        prato.setNome(comando.getNome());
        prato.setDescricao(comando.getDescricao());
        prato.setPreco(comando.getPreco());
        prato.setDisponivelSomenteNoLocal(comando.getDisponivelSomenteNoLocal());
        prato.setRestaurante(restaurante);

        Prato salvo = pratoRepository.save(prato);
        return PratoResponseDTO.fromEntity(salvo);
    }
}
