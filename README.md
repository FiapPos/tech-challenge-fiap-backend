# 🍽️ FoodSys – Tech Challenge - FIAP

## Equipe

- [Gabriel Alves de Souza](https://github.com/devgabrielsouza4278)
- [Gustavo Lima Aliba](https://github.com/GustavoLimaAl)
- [Julio Cesar Salerno da Silva](https://github.com/jcsalerno)
- [Sonia Alves Ribeiro](https://github.com/hopesoh)
- [Stephanie Ingrid Menezes](https://github.com/steingcam)

---

## 1. Introdução

### 📌 Descrição do Problema

O **FoodSys** é um sistema compartilhado de gestão de estabelecimentos para um grupo de restaurantes. Seu objetivo é:

* Reduzir custos com sistemas individuais
* Permitir que os clientes escolham o restaurante com base na comida, não na qualidade do sistema

### 🌟 Objetivo do Projeto

Desenvolver um sistema **backend robusto** com **Spring Boot** que possibilite:

* Gestão de usuários (CRUD + login)
* Gestão de endereços dos usuários (CRUD)

---

## 2. Arquitetura do Sistema

### 🛠️ Tecnologias Utilizadas

* **Linguagem**: Java 21 com Spring Framework

    * Spring Web (REST APIs)
    * Spring Data JPA
    * Spring Security (JWT)
    * Lombok
* **Banco de Dados**: H2 (em memória)
* **Containerização**: Docker

    * Imagem base: `eclipse-temurin:21-jdk-jammy`

### 🧱 Camadas da Aplicação

* `config/` – Configurações de segurança, JWT e mensagens
* `controller/` – Camada de controle (REST endpoints)
* `dto/` – Objetos de transferência de dados
* `enums/` – Constantes e enums
* `exception/` – Tratamento global de exceções
* `dominio/` – Entidades de domínio
* `query/` – Consultas especializadas
* `utils/` – Utilitários e validações
* `compartilhado/` – Serviços e componentes reutilizáveis

---

## 3. Endpoints da API

| Endpoint                         | Método | Descrição               |
| -------------------------------- | ------ | ----------------------- |
| `/usuarios`                      | POST   | Criação de usuários     |
| `/usuarios/{usuarioId}`          | PUT    | Atualização de usuários |
| `/usuarios?ativo=`               | GET    | Listagem de usuários    |
| `/usuarios/{usuarioId}`          | DELETE | Desativação de usuário  |
| `/login`                         | POST   | Autenticação            |
| `/login/atualiza-senha`          | PUT    | Atualização de senha    |
| `/enderecos`                     | POST   | Criação de endereço     |
| `/enderecos/{enderecoId}`        | PUT    | Atualização de endereço |
| `/enderecos/usuario/{usuarioId}` | GET    | Listagem de endereços   |
| `/enderecos`                     | DELETE | Exclusão de endereço    |

### 📄 Documentação Swagger

Acesse a documentação interativa da API:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

### Documentação Geral

🔗 https://onedrive.live.com/personal/7b17063f7fd73beb/_layouts/15/Doc.aspx?sourcedoc=%7Be62aa878-96c5-4a84-b2e1-5eae65ce200d%7D&action=default&redeem=aHR0cHM6Ly8xZHJ2Lm1zL3cvYy83YjE3MDYzZjdmZDczYmViL0VYaW9LdWJGbG9SS3N1RmVybVhPSUEwQm9lQVh5enU4cGlCRGhJNVdHMzFjNmc_ZT1NOE4zMGY&slrid=ab4ca3a1-5056-0000-bad2-5acfb3f5102f&originalPath=aHR0cHM6Ly8xZHJ2Lm1zL3cvYy83YjE3MDYzZjdmZDczYmViL0VYaW9LdWJGbG9SS3N1RmVybVhPSUEwQm9lQVh5enU4cGlCRGhJNVdHMzFjNmc_cnRpbWU9V04zdkxxNmYzVWc&CID=582f6299-a723-4ce3-812f-ad523369a803&_SRM=0:G:44

---

## 4. Configuração do Projeto

### 🐳 Docker

* **Imagem base**: `eclipse-temurin:21-jdk-jammy`
* **Porta**: 8080 (exposta como 8082)
* **Variáveis de ambiente**:

    * Banco H2 (em memória)
    * Credenciais Admin:

        * `Usuário`: `admin`
        * `Senha`: `12345`

### ▶️ Execução Local

#### Pré-requisitos

* Docker **https://docs.docker.com/get-started/get-docker/**
* Docker Compose **https://docs.docker.com/compose/install/**

#### Como Rodar o Projeto

```bash
# Clone o repositório
git clone https://github.com/FiapPos/tech-challenge-fiap-backend.git

# Entre na pasta do projeto
cd tech-challenge-fiap-backend

# Inicie o serviço via Docker Compose
docker-compose up -d foodsys-api
```

### 📃 Acesso ao Banco H2

* URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* JDBC URL: `jdbc:h2:mem:foodsys`
* Usuário: `sa`
* Senha: `senhasegura`

---

## 5. Qualidade do Código

### 🧠 Princípios SOLID

* **SRP**: Classes com responsabilidade única
* **OCP**: Aberto para extensão, fechado para modificação
* **LSP**: Substituibilidade de subclasses
* **ISP**: Interfaces específicas para cada cliente
* **DIP**: Inversão de dependência

### 🧰 Padrões de Projeto

* Repository Pattern
* DTO Pattern
* Command Pattern
* Exception Handling centralizado

### ✅ Boas Práticas

* Nomenclatura RESTful
* Validações robustas
* Documentação Swagger
* Segurança com JWT

---

## 6. Testes

### 📦 Coleção Postman

Você pode testar a API importando a coleção disponível em:

🔗 [techchallenge.foodsys.postman\_collection.json](https://github.com/FiapPos/tech-challenge-fiap-backend/blob/main/techchallenge.foodsys.postman_collection.json)

---
## ✅ Status do Projeto

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?logo=springboot)
![Docker](https://img.shields.io/badge/docker-ready-blue?logo=docker)
![JWT](https://img.shields.io/badge/Auth-JWT-green)
![License](https://img.shields.io/github/license/FiapPos/tech-challenge-fiap-backend)
[![Postman Collection](https://img.shields.io/badge/Postman-Collection-orange?logo=postman)](https://github.com/FiapPos/tech-challenge-fiap-backend/blob/main/techchallenge.foodsys.postman_collection.json)

---