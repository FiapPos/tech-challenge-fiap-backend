
ALTER TABLE usuario DROP COLUMN tipo;

CREATE TABLE usuario_tipo (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    CONSTRAINT fk_usuario_tipo_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE INDEX idx_usuario_tipo_usuario_id ON usuario_tipo (usuario_id);