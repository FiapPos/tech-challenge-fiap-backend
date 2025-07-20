-- Inserindo usuários de teste
INSERT INTO usuario (nome, email, senha, login, ativo, data_criacao, data_atualizacao) VALUES
('Admin Sistema', 'admin@foodsys.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'admin', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('João Silva - Dono', 'joao.silva@email.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'joao.dono', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Maria Santos - Dona', 'maria.santos@email.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'maria.dona', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Carlos Funcionário', 'carlos.funcionario@email.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'carlos.func', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ana Cliente', 'ana.cliente@email.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'ana.cliente', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pedro Cliente', 'pedro.cliente@email.com', '$2a$10$N.zmdr9k7uOCQxVsHj0OjeLKtGFh7bvVvC4wr7WmwXWwrmFdvKt1q', 'pedro.cliente', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserindo tipos de usuário
INSERT INTO usuario_tipo (usuario_id, tipo) VALUES
(1, 'ADMIN'),
(2, 'DONO_RESTAURANTE'),
(3, 'DONO_RESTAURANTE'),
(4, 'FUNCIONARIO'),
(5, 'CLIENTE'),
(6, 'CLIENTE');

-- Inserindo restaurantes
INSERT INTO restaurante (nome, tipo_cozinha, horario_abertura, horario_fechamento, usuario_id, ativo, data_criacao, data_atualizacao) VALUES
('Pizzaria do João', 'Italiana', '18:00', '23:00', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurante da Maria', 'Brasileira', '11:00', '22:00', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lanchonete Central', 'Fast Food', '10:00', '24:00', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserindo endereços para usuários
INSERT INTO endereco (rua, cep, numero, usuario_id, data_criacao, data_atualizacao) VALUES
('Rua Admin', '01234567', '100', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rua das Pizzas', '12345678', '200', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Av. Brasil', '23456789', '300', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rua dos Funcionários', '34567890', '400', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rua das Flores', '45678901', '500', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Av. Paulista', '56789012', '600', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserindo endereços para restaurantes
INSERT INTO endereco (rua, cep, numero, restaurante_id, data_criacao, data_atualizacao) VALUES
('Rua das Pizzas', '12345678', '200', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Av. Brasil', '23456789', '300', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rua Central', '67890123', '150', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserindo itens do cardápio
INSERT INTO prato (nome, descricao, preco, restaurante_id, data_criacao, data_atualizacao) VALUES
('Pizza Margherita', 'Pizza tradicional com molho de tomate, mussarela e manjericão', 35.90, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pizza Calabresa', 'Pizza com calabresa, cebola e azeitonas', 38.90, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lasanha da Casa', 'Lasanha de carne com molho bolonhesa', 42.90, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Feijoada Completa', 'Feijoada tradicional com acompanhamentos', 55.90, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Picanha Grelhada', 'Picanha grelhada com arroz, feijão e farofa', 68.90, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Moqueca de Peixe', 'Moqueca baiana com peixe fresco', 49.90, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('X-Burguer', 'Hambúrguer artesanal com batata frita', 25.90, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hot Dog Especial', 'Hot dog com molhos especiais', 18.90, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Açaí na Tigela', 'Açaí com granola e frutas', 15.90, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
