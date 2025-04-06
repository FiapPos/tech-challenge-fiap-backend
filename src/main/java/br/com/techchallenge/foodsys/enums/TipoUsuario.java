package br.com.techchallenge.foodsys.enums;

import java.util.Arrays;

public enum TipoUsuario {
    ADMIN(1), CLIENTE(2), FUNCIONARIO(3);

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