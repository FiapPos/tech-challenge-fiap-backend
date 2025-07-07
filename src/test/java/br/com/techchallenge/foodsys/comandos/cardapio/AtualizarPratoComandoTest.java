package br.com.techchallenge.foodsys.comandos.cardapio;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AtualizarPratoComandoTest {

    @Test
    void deveCriarAtualizarPratoComandoComDadosCorretos() {
        String nome = "Pizza Marguerita";
        String descricao = "Pizza clássica com tomate, mussarela e manjericão";
        BigDecimal preco = new BigDecimal("39.90");
        Boolean disponivelNoLocal = true;

        AtualizarPratoComando comando = new AtualizarPratoComando(nome, descricao, preco, disponivelNoLocal);

        assertEquals(nome, comando.getNome());
        assertEquals(descricao, comando.getDescricao());
        assertEquals(preco, comando.getPreco());
        assertEquals(disponivelNoLocal, comando.getDisponivelSomenteNoLocal());
    }
}
