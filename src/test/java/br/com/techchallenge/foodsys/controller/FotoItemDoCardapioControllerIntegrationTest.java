package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test" )
@Transactional
class FotoItemDoCardapioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ItemDoCardapioRepository itemDoCardapioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Restaurante restauranteSalvo;
    private ItemDoCardapio itemSalvo;

    @BeforeEach
    void setup() {
        Usuario dono = new Usuario();
        dono.setNome("Dono Foto Teste");
        dono.setEmail("donofoto@teste.com");
        dono.setLogin("dono.foto.teste");
        dono.setSenha("senha123");
        dono.setTipo(TipoUsuario.ADMIN);
        Usuario donoSalvo = usuarioRepository.save(dono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante da Foto");
        restaurante.setTipoCozinha("Fotogenica");
        restaurante.setUsuario(donoSalvo);
        restauranteSalvo = restauranteRepository.save(restaurante);

        ItemDoCardapio item = new ItemDoCardapio();
        item.setNome("Prato para Foto");
        item.setDescricao("Descrição do prato");
        item.setPreco(new BigDecimal("25.00"));
        item.setRestaurante(restauranteSalvo);
        itemSalvo = itemDoCardapioRepository.saveAndFlush(item);
    }

    @Test
    @DisplayName("Deve Fazer Upload da Foto para um Item de Cardápio com Sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveFazerUploadDeFotoComSucesso() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "arquivo",
                "foto-prato.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteúdo da imagem em bytes".getBytes()
        );

        mockMvc.perform(multipart("/restaurantes/{restauranteId}/itens/{itemId}/foto",
                        restauranteSalvo.getId(), itemSalvo.getId())
                        .file(mockFile))
                .andExpect(status().isCreated())
                .andExpect(content().string("Foto salva com sucesso!"));
    }

    @Test
    @DisplayName("Deve Retornar 404 ao Tentar Fazer Upload para um Item Inexistente")
    @WithMockUser(roles = "ADMIN")
    void deveRetornarNotFoundParaItemInexistente() throws Exception {
        long idItemInexistente = 999L;
        MockMultipartFile mockFile = new MockMultipartFile("arquivo", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "imagem".getBytes());

        mockMvc.perform(multipart("/restaurantes/{restauranteId}/itens/{itemId}/foto",
                        restauranteSalvo.getId(), idItemInexistente)
                        .file(mockFile))
                .andExpect(status().isNotFound());
    }
}
