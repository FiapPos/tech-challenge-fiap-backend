package br.com.techchallenge.foodsys.core.enums;

import java.util.Arrays;

public enum TipoUsuario {
    ADMIN(0), CLIENTE(1), FUNCIONARIO(2), DONO_RESTAURANTE(3);

    private final int codigo;

    TipoUsuario(int codigo) { this.codigo = codigo; }
    public int getCodigo() { return codigo; }

    public static TipoUsuario fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(t -> t.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código inválido: " + codigo));
    }
}