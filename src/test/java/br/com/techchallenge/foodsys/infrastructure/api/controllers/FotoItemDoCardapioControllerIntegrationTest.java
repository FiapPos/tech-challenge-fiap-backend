package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.entities.*;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.gateways.FotoPratoRepository;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    @MockBean
    private FotoPratoRepository fotoPratoRepository;

    private Restaurante restauranteSalvo;
    private ItemDoCardapio itemSalvo;
    private Usuario usuarioLogado;

    @BeforeEach
    void setup() {
        Usuario dono = new Usuario();
        dono.setNome("Dono Foto Teste");
        dono.setEmail("donofoto@teste.com");
        dono.setLogin("dono.foto.teste");
        dono.setSenha("senha123");
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(dono);
        usuarioTipo.setTipo(TipoUsuario.ADMIN);
        dono.setUsuarioTipos(Set.of(usuarioTipo));
        usuarioLogado = usuarioRepository.save(dono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante da Foto");
        restaurante.setTipoCozinha("Fotogenica");
        restaurante.setUsuario(usuarioLogado);
        restauranteSalvo = restauranteRepository.save(restaurante);

        ItemDoCardapio item = new ItemDoCardapio();
        item.setNome("Prato para Foto");
        item.setDescricao("Descrição do prato");
        item.setPreco(new BigDecimal("25.00"));
        item.setRestaurante(restauranteSalvo);
        itemSalvo = itemDoCardapioRepository.save(item);

        when(fotoPratoRepository.save(any(FotoPratoDocumento.class))).thenReturn(new FotoPratoDocumento());
    }

    private void configurarAutenticacao() {
        DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(usuarioLogado);
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication = 
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Deve Fazer Upload da Foto para um Item de Cardápio com Sucesso")
    void deveFazerUploadDeFotoComSucesso() throws Exception {
        configurarAutenticacao();
        
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
    void deveRetornarNotFoundParaItemInexistente() throws Exception {
        configurarAutenticacao();
        
        long idItemInexistente = 999L;
        MockMultipartFile mockFile = new MockMultipartFile("arquivo", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "imagem".getBytes());

        mockMvc.perform(multipart("/restaurantes/{restauranteId}/itens/{itemId}/foto",
                        restauranteSalvo.getId(), idItemInexistente)
                        .file(mockFile))
                .andExpect(status().isUnauthorized());
    }
}
