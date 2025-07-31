package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.cardapio.FotoItemDoCardapioHandler;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FotoItemDoCardapioControllerTest {

    private MockMvc mockMvc;
    private FotoItemDoCardapioHandler fotoItemDoCardapioHandler;
    private ValidadorPermissoes validadorPermissoes;

    @BeforeEach
    void setUp() {
        fotoItemDoCardapioHandler = Mockito.mock(FotoItemDoCardapioHandler.class);
        validadorPermissoes = Mockito.mock(ValidadorPermissoes.class);
        FotoItemDoCardapioController controller = new FotoItemDoCardapioController(fotoItemDoCardapioHandler, validadorPermissoes);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveRetornar201QuandoUploadForSucesso() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(anyLong());
        doNothing().when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/itens/1/foto")
                        .file(arquivo))
                .andExpect(status().isCreated())
                .andExpect(content().string("Foto salva com sucesso!"));
    }

    @Test
    void deveRetornar500QuandoIOException() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(anyLong());
        doThrow(IOException.class).when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/itens/1/foto")
                        .file(arquivo))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro ao salvar a foto."));
    }

    @Test
    void deveRetornar404QuandoRuntimeException() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(anyLong());
        doThrow(new RuntimeException("Prato não encontrado")).when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/itens/1/foto")
                        .file(arquivo))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Prato não encontrado"));
    }
}
