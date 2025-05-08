CREATE TABLE endereco (
    idEndereco BIGSERIAL PRIMARY KEY,
    rua VARCHAR(255),
    cep VARCHAR(20),
    numero VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);