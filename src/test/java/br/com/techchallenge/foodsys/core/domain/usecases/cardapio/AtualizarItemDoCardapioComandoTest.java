package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AtualizarItemDoCardapioComandoTest {

    @Test
    void deveCriarAtualizarPratoComandoComDadosCorretos() {
        String nome = "Pizza Marguerita";
        String descricao = "Pizza clássica com tomate, mussarela e manjericão";
        BigDecimal preco = new BigDecimal("39.90");
        Boolean disponivelNoLocal = true;

        AtualizarItemDoCardapioComando comando = new AtualizarItemDoCardapioComando(nome, descricao, preco, disponivelNoLocal);

        assertEquals(nome, comando.getNome());
        assertEquals(descricao, comando.getDescricao());
        assertEquals(preco, comando.getPreco());
        assertEquals(disponivelNoLocal, comando.getDisponivelSomenteNoLocal());
    }
}
