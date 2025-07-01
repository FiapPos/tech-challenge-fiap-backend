CREATE TABLE prato (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    imagem_url TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    restaurante_id BIGINT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,

    CONSTRAINT fk_restaurante_prato
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurante(id)
        ON DELETE CASCADE
);
