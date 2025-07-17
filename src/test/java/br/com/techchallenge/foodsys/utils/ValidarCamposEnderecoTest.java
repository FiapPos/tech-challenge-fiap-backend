package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidarCamposEnderecoTest {

    private final ValidarCamposEndereco validarCamposEndereco = new ValidarCamposEndereco();

    @Test
    void deveLancarExcecaoQuandoTodosOsCamposForemNulos() {
        assertThrows(BadRequestException.class, () -> validarCamposEndereco.validar(null, null, null));
    }

    @Test
    void naoDeveLancarExcecaoQuandoRuaNaoForNula() {
        validarCamposEndereco.validar("Rua A", null, null);
    }

    @Test
    void naoDeveLancarExcecaoQuandoCepNaoForNulo() {
        validarCamposEndereco.validar(null, "12345-678", null);
    }

    @Test
    void naoDeveLancarExcecaoQuandoNumeroNaoForNulo() {
        validarCamposEndereco.validar(null, null, "100");
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosOsCamposNaoForemNulos() {
        validarCamposEndereco.validar("Rua B", "98765-432", "200");
    }
}