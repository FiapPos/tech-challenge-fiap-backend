package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemDoCardapioEntityTest {

    private ItemDoCardapioEntity itemEntity;
    private ItemDoCardapio item;
    private RestauranteEntity restauranteEntity;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        itemEntity = new ItemDoCardapioEntity();
        item = new ItemDoCardapio();
        restauranteEntity = new RestauranteEntity();
        restaurante = new Restaurante();

        // Configurar dados básicos do item
        itemEntity.setId(1L);
        itemEntity.setNome("Pizza Margherita");
        itemEntity.setDescricao("Pizza tradicional com molho de tomate, mussarela e manjericão");
        itemEntity.setPreco(new BigDecimal("25.90"));
        itemEntity.setDisponivelSomenteNoLocal(true);
        itemEntity.setCaminhoFoto("/fotos/pizza-margherita.jpg");

        item.setId(1L);
        item.setNome("Pizza Margherita");
        item.setDescricao("Pizza tradicional com molho de tomate, mussarela e manjericão");
        item.setPreco(new BigDecimal("25.90"));
        item.setDisponivelSomenteNoLocal(true);
        item.setCaminhoFoto("/fotos/pizza-margherita.jpg");

        // Configurar restaurante
        restauranteEntity.setId(1L);
        restauranteEntity.setNome("Pizzaria Teste");
        restauranteEntity.setTipoCozinha("Italiana");
        restauranteEntity.setAtivo(true);

        restaurante.setId(1L);
        restaurante.setNome("Pizzaria Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setAtivo(true);
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // Given
        itemEntity.setRestaurante(restauranteEntity);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(itemEntity.getId(), resultado.getId());
        assertEquals(itemEntity.getNome(), resultado.getNome());
        assertEquals(itemEntity.getDescricao(), resultado.getDescricao());
        assertEquals(itemEntity.getPreco(), resultado.getPreco());
        assertEquals(itemEntity.getDisponivelSomenteNoLocal(), resultado.getDisponivelSomenteNoLocal());
        assertEquals(itemEntity.getCaminhoFoto(), resultado.getCaminhoFoto());
        assertNotNull(resultado.getRestaurante());
        assertEquals(restauranteEntity.getId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveConverterParaDomainSemRestaurante() {
        // Given
        itemEntity.setRestaurante(null);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(itemEntity.getId(), resultado.getId());
        assertEquals(itemEntity.getNome(), resultado.getNome());
        assertEquals(itemEntity.getDescricao(), resultado.getDescricao());
        assertEquals(itemEntity.getPreco(), resultado.getPreco());
        assertEquals(itemEntity.getDisponivelSomenteNoLocal(), resultado.getDisponivelSomenteNoLocal());
        assertEquals(itemEntity.getCaminhoFoto(), resultado.getCaminhoFoto());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // Given
        item.setRestaurante(restaurante);

        // When
        ItemDoCardapioEntity resultado = ItemDoCardapioEntity.fromDomain(item);

        // Then
        assertNotNull(resultado);
        assertEquals(item.getId(), resultado.getId());
        assertEquals(item.getNome(), resultado.getNome());
        assertEquals(item.getDescricao(), resultado.getDescricao());
        assertEquals(item.getPreco(), resultado.getPreco());
        assertEquals(item.getDisponivelSomenteNoLocal(), resultado.getDisponivelSomenteNoLocal());
        assertEquals(item.getCaminhoFoto(), resultado.getCaminhoFoto());
        assertNotNull(resultado.getRestaurante());
        assertEquals(restaurante.getId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveConverterDeDomainSemRestaurante() {
        // Given
        item.setRestaurante(null);

        // When
        ItemDoCardapioEntity resultado = ItemDoCardapioEntity.fromDomain(item);

        // Then
        assertNotNull(resultado);
        assertEquals(item.getId(), resultado.getId());
        assertEquals(item.getNome(), resultado.getNome());
        assertEquals(item.getDescricao(), resultado.getDescricao());
        assertEquals(item.getPreco(), resultado.getPreco());
        assertEquals(item.getDisponivelSomenteNoLocal(), resultado.getDisponivelSomenteNoLocal());
        assertEquals(item.getCaminhoFoto(), resultado.getCaminhoFoto());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        ItemDoCardapioEntity entity = new ItemDoCardapioEntity();

        // Then
        assertNull(entity.getId());
        assertNull(entity.getNome());
        assertNull(entity.getDescricao());
        assertNull(entity.getPreco());
        assertNull(entity.getDisponivelSomenteNoLocal());
        assertNull(entity.getCaminhoFoto());
        assertNull(entity.getRestaurante());
    }

    @Test
    void deveConverterItemComTodosOsCampos() {
        // Given
        itemEntity.setId(1L);
        itemEntity.setNome("Hambúrguer Gourmet");
        itemEntity.setDescricao("Hambúrguer artesanal com pão brioche, carne angus e queijo cheddar");
        itemEntity.setPreco(new BigDecimal("35.50"));
        itemEntity.setDisponivelSomenteNoLocal(false);
        itemEntity.setCaminhoFoto("/fotos/hamburguer-gourmet.jpg");
        itemEntity.setRestaurante(restauranteEntity);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hambúrguer Gourmet", resultado.getNome());
        assertEquals("Hambúrguer artesanal com pão brioche, carne angus e queijo cheddar", resultado.getDescricao());
        assertEquals(new BigDecimal("35.50"), resultado.getPreco());
        assertFalse(resultado.getDisponivelSomenteNoLocal());
        assertEquals("/fotos/hamburguer-gourmet.jpg", resultado.getCaminhoFoto());
        assertNotNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterItemComPrecoNull() {
        // Given
        itemEntity.setPreco(null);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getPreco());
    }

    @Test
    void deveConverterItemComDisponivelSomenteNoLocalNull() {
        // Given
        itemEntity.setDisponivelSomenteNoLocal(null);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveConverterItemComCaminhoFotoNull() {
        // Given
        itemEntity.setCaminhoFoto(null);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getCaminhoFoto());
    }

    @Test
    void deveConverterItemComDescricaoNull() {
        // Given
        itemEntity.setDescricao(null);

        // When
        ItemDoCardapio resultado = itemEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getDescricao());
    }

    @Test
    void deveTestarGettersESetters() {
        // Given
        ItemDoCardapioEntity entity = new ItemDoCardapioEntity();

        // When
        entity.setId(1L);
        entity.setNome("Teste");
        entity.setDescricao("Descrição teste");
        entity.setPreco(new BigDecimal("10.00"));
        entity.setDisponivelSomenteNoLocal(true);
        entity.setCaminhoFoto("/teste.jpg");
        entity.setRestaurante(restauranteEntity);

        // Then
        assertEquals(1L, entity.getId());
        assertEquals("Teste", entity.getNome());
        assertEquals("Descrição teste", entity.getDescricao());
        assertEquals(new BigDecimal("10.00"), entity.getPreco());
        assertTrue(entity.getDisponivelSomenteNoLocal());
        assertEquals("/teste.jpg", entity.getCaminhoFoto());
        assertEquals(restauranteEntity, entity.getRestaurante());
    }
} 