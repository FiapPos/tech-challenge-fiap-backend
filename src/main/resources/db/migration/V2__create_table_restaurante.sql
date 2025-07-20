CREATE TABLE restaurante (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(155),
    tipo_cozinha VARCHAR(100),
    horario_abertura VARCHAR(50),
    horario_fechamento VARCHAR(50),
    usuario_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_desativacao TIMESTAMP,
    CONSTRAINT fk_usuario_restaurante FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);